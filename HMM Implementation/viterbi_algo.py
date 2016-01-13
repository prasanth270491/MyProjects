#!/usr/bin/python

import sys
import math

observed_tags = {} # Map of observed tags for a given word
count_words_tags = {} #words/tags  mapped to their counts
count_tw = {} #(tag, word) tuples mapped to their counts
sing_tw = {} #Map of singletons, sing(.|ti)
count_tt = {} #(tag, previous_tag) tuples mapped to their counts
sing_tt = {} #Map of singletons, sing(.|ti-1)
tag_count = 1
tag_types = 1

def calculate_counts(entrain_file):
    tags = []
    words = []

    try:
        file_ptr = open(entrain_file, 'r')

        for line in file_ptr:
            (word, tag) = line.strip().split('/')
            tags.append(tag)
            words.append(word)
    
    except IOError:
        sys.exit("Not able to open file  %s" % (entrain_file))

    file_ptr.close()
    
    tag_count = len(tags) - 1 

    observed_tags[words[0]] = [tags[0]]
    observed_tags['ALL'] = []
    
    count_words_tags[words[0]] = 1
    count_words_tags[tags[0]] = 1
    
    tw = tags[0] + '/' + words[0]
    count_tw[tw] = 1
    sing_tw[tags[0]] = 1

    for i in range(1, len(words)):

        tw = tags[i] + '/' + words[i]

        if (tags[i] not in observed_tags['ALL']):
            if (tags[i] != '###'):
                observed_tags['ALL'].append(tags[i])

        if ((count_tw.get(tw, 0) == 0) and (words[i] not in observed_tags)):
            observed_tags[words[i]]= [tags[i]]
        elif (count_tw.get(tw, 0) == 0):
            observed_tags[words[i]].append(tags[i])

        count_tw[tw] = count_tw.get(tw, 0) + 1

        if (count_tw[tw] == 1):
            sing_tw[tags[i]] = sing_tw.get(tags[i], 0) + 1
        elif (count_tw[tw] == 2):
            sing_tw[tags[i]] -= 1

        for key in [words[i], tags[i]]:
            count_words_tags[key] = count_words_tags.get(key, 0) + 1

        tt = tags[i-1] + '/' + tags[i]
        count_tt[tt] = count_tt.get(tt, 0) + 1

        if (count_tt[tt] == 1):
            sing_tt[tags[i-1]] = sing_tt.get(tags[i-1], 0) + 1
        elif (count_tt[tt] == 2):
            sing_tt[tags[i-1]] -= 1

    tag_types = len(observed_tags.keys()) 
    
    count_words_tags['###'] = count_words_tags['###'] / 2

def transition_prob(i, j):
        tt = i + '/' + j
        backoff = float(count_words_tags[j])/tag_count
        lambdap = sing_tt[i] + 1e-100
        return math.log(float(count_tt.get(tt, 0) + lambdap*backoff)/(count_words_tags[i] + lambdap))

def observation_prob(i,j):
        tw = i + '/' + j
        backoff = float(count_words_tags.get(j, 0) + 1)/(tag_count+tag_types)
        lambdap = sing_tw[i] + 1e-100
        return math.log(float(count_tw.get(tw, 0)+lambdap*backoff)/(count_words_tags[i] + lambdap)) 
    
def viterbi_algo(train_file, test_file):
    
    #Determines best tag sequence given observations using
    #the Viterbi algorithm.
    
    test_words = []
    test_tags = []

    try:
        file_ptr = open(test_file, 'r')

        for line in file_ptr:
            (word, tag) = line.strip().split('/')
            test_tags.append(tag)
            test_words.append(word)

    except IOError:
        sys.exit("Not able to open file  %s" % (test))

    calculate_counts(train_file)
    
    neg_infinity = float('-inf')
    viterbi_values = {} # to store viterbi values
    back_ptr = {}   # to store backpointers
    tran_prob = {}     # transition probabilities
    obs_prob = {}      # observation probabilities

    viterbi_values['0/###']= 1.0
    back_ptr['0/###'] = None
    for tag in observed_tags[test_words[1]]:
        viterbi_values['1' + '/' + tag] = transition_prob('###', tag) + observation_prob(tag, test_words[1])
        back_ptr['1' + '/' + tag] = '###'

    for j in range(2, len(test_words)):
        for tj in observed_tags.get(test_words[j], observed_tags['ALL']):        # For each possible tag tj for current test_words at j

            vj = str(j) + '/' + tj
            for ti in observed_tags.get(test_words[j-1], observed_tags['ALL']):  # For each possible previous tag ti leading to current tag tj
                
                vi = str(j-1) + '/' +  ti
                tt = ti + '/' + tj
                tw = tj + '/' + test_words[j]

                if tt not in tran_prob:
                    tran_prob[tt] = transition_prob(ti,tj)
                if tw not in obs_prob:
                    obs_prob[tw] = observation_prob(tj,test_words[j])

                u = viterbi_values[vi] + tran_prob[tt] + obs_prob[tw]
    
                if u > viterbi_values.get(vj, neg_infinity):     # If u is max so far, set it so,
                    viterbi_values[vj] = u
                    back_ptr[str(j) + '/' + tj] = ti    # and store backpointer to ti that gave that u

    predict_value = ['###']
    prev = predict_value[0]
    known, novel, ktotal, ntotal = 0, 0, 0, 0

    # Follow backpointers to find most likely sequence.
    for i in xrange(len(test_words)-1, 0, -1):
        
        if ((test_words[i] in count_words_tags) and (test_words[i] != '###')):
            ktotal += 1
            if predict_value[0] == test_tags[i]:
                known += 1
        if(test_words[i] != '###'):
            ntotal += 1
            if predict_value[0] == test_tags[i]:
                novel += 1

        tag = back_ptr[str(i) + '/' +  prev]
        predict_value.insert(0, tag)
        prev = tag
    accuracy = float(known + novel) / (ktotal + ntotal) * 100
    error_rate = 100 - accuracy
    matching_word_count = (ktotal + ntotal) - (known + novel)
    total_word_count = ktotal + ntotal
    
    return (error_rate,matching_word_count,total_word_count) 

def main():

    entrain_file = "entrain.txt"
    entest_file = "entest.txt"
 
    (error_rate,matching_word_count,total_word_count) = viterbi_algo(entrain_file, entest_file)
    print "Not Matching Word Count : " + str(matching_word_count) + " Total Word Count : " + str(total_word_count)
    print "Error Rate: %.4g%% " % error_rate
    
if __name__ == "__main__":
    main()
