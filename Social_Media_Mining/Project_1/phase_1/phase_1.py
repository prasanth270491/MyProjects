from __future__ import unicode_literals

import requests
import json
import time
import codecs
import sys
UTF8Writer = codecs.getwriter('utf8')
sys.stdout = UTF8Writer(sys.stdout)

def main():

        cities =[("Bridgeport","CT")]
        api_key= "c695b2f672934d05cb6f7419644e"
        group_id = []
        group_name = []
        member_id = []
        member_name = []
        topics = []
        visited = []
        for (city, state) in cities:
            per_page = 200
            results_we_got = per_page
            offset = 0
            index = 0
            while (results_we_got == per_page):
                response=get_groups({"sign":"true","country":"US", "city":city, "state":state, "radius": 10, "key":api_key, "page":per_page, "offset":offset })
                time.sleep(1)
                offset += 1
                results_we_got = response['meta']['count']
                for group in response['results']:
                    if group['members'] >= 800:
                        response_members = get_members({"group_id":group['id'],"key":api_key, "page":per_page, "offset":offset})
                        for member in response_members['results']:
                            group_id.append(group['id'])
                            group_name.append(group['name'])
                            member_id.append(member['id'])
                            member_name.append(member['name'])
                            topic_id = []
                            for topic in member['topics']:
                                topic_id.append(topic['id'])
                            topics.append(topic_id)
        time.sleep(1)
   
        f1 = open('phase_1_output/anonymized_edge_list.txt', 'w')
        f2 = open('phase_1_output/anonymized.txt', 'w')
        f3 = open('phase_1_output/edge_list.txt', 'w')
        f4 = open('phase_1_output/sampled.txt', 'w')
        f5 = open('phase_1_output/anonymized_sampled.txt', 'w')

        # Breadth First Search to get the edge list of members
        # An edge between members is formed if there are more than 5 interests(topics) shared between the members
        visited = set()
        queue = [0]
        while queue:
              index = queue.pop(0)
              if index not in visited:
                 visited.add(index)
                 child = 0
                 for i in range(len(member_id)):
                     if child >= 1000:
                        f4.write(str(member_id[index]) + ',' + str(child))
                        f4.write('\n')
                        f5.write(str(index+1) + ',' + str(child))
                        f5.write('\n')
                        break;
                     if i != index and i not in visited:
                        count = 0
                        for topic_id in topics[index]:
                            if topic_id in topics[i]:
                               count = count + 1
                        if count > 5:
                            f1.write(str(index+1) + ',' + str(i+1))
                            f1.write('\n')
                            f2.write(str(member_id[index]) + ','  + str(index+1))
                            f2.write('\n')
                            f3.write(str(member_id[index]) + ',' + str(member_id[i]))
                            f3.write('\n')
                            queue.append(i);
                            child += 1

def get_groups(params):

	request = requests.get("http://api.meetup.com/2/groups",params=params)
        data = request.json()
	return data

def get_members(params):

        request = requests.get("http://api.meetup.com/2/members",params=params)
        data = request.json()
        return data
        

if __name__=="__main__":
        main()


