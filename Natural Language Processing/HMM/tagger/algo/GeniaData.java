package tagger.algo;

import tagger.Activity;
import tagger.domain.Constants;
import tagger.domain.Sentence;
import tagger.domain.Tag;
import tagger.domain.Word;
import java.lang.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tagger.*;
/**
 * Created with IntelliJ IDEA.
 * User: ferhataydin
 * Date: 11/12/13
 */

public class GeniaData {

    private List<Sentence> sentenceList = new ArrayList<Sentence>();

    private Map<Tag, Integer> tagMap = new HashMap<Tag, Integer>();
    private ArrayList<String> actList = new ArrayList<String>();

    private Map<Word, Map<Tag, Integer>> wordTagCountMap = new HashMap<Word, Map<Tag, Integer>>();

    private Map<String,Integer> prevActivities=new HashMap<String,Integer>();

    private Map<String,Integer> nextActivities = new HashMap<String,Integer>();
    private Map<String,Integer> participants = new HashMap<String,Integer>();
    private List<String> similarActivity=new ArrayList<String>();
    public List<Sentence> getSentenceList() {
        return sentenceList;
    }

    public Map<Tag, Integer> getTagMap() {
        return tagMap;
    }
    public void printSimilarActivities(String fileName)
    {
        BufferedReader br = null;

        try
        {
            //PrintWriter writer = new PrintWriter("Out3.txt", "UTF-8");
            String fileLine;
            br = new BufferedReader(new FileReader(fileName));

                while ((fileLine = br.readLine())!=null)
                {
                     if(fileLine==null)
                         break;
                        actList.add(fileLine+".txt");

                }
                System.out.println(actList);
            }

        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    public void readGeniaFile(String fileName) {

        BufferedReader br = null;
       // PrintWriter writer1 = new PrintWriter("Out4.txt", "UTF-8");
        System.out.println("Print This"+fileName);
        try {
            //PrintWriter writer1 = new PrintWriter("Out4.txt", "UTF-8");
            String fileLine;
            br = new BufferedReader(new FileReader(fileName));
            int curPos=0;
            while ((fileLine = br.readLine()) != null) {

                Sentence sentence  = new Sentence();

                while (!(fileLine = br.readLine()).equals(Constants.SENTENCE_SEPERATOR))
                {
                     if(fileLine==null)
                         break;
                    //take words as one part, they may include more than one word
                    String word = fileLine.substring(0, fileLine.lastIndexOf(Constants.WORD_TAG_SEPERATOR));
                    sentence.getWords().add(new Word(word.toLowerCase()));

                    String tag = fileLine.substring(fileLine.lastIndexOf(Constants.WORD_TAG_SEPERATOR) + 1);
                    //tags may be like tag1|tag2, take first one as analysis design
                    if (tag.contains(Constants.MULTI_TAG_SEPERATOR)) {

                        String tagFirst = tag.substring(0, tag.lastIndexOf(Constants.MULTI_TAG_SEPERATOR));
                        sentence.getTags().add(new Tag(tagFirst));

                    } else {

                        sentence.getTags().add(new Tag(tag));
                    }

                  //  sentence.getTags().add(new Tag(tag));

                }
                sentence.getWords().add(new Word(Constants.SENTENCE_END));
                sentence.getTags().add(new Tag(Constants.SENTENCE_END));
                sentenceList.add(sentence);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void createTagMap() {
         for (Sentence sentence : sentenceList) {

         for (Tag tag : sentence.getTags()) {

                if (tagMap.containsKey(tag)) {

                    tagMap.put(tag, tagMap.get(tag) + 1);

                } else {

                    tagMap.put(tag, 1);
                }

            }
         }
    }

    private Map<String, Integer> sortByValues(Map<String, Integer> map) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List list = new LinkedList(map.entrySet());
       // Defined Custom Comparator here
       Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o1)).getValue())
                  .compareTo(((Map.Entry) (o2)).getValue());
            }
       });

       // Here I am copying the sorted list in HashMap
       // using LinkedHashMap to preserve the insertion order
       HashMap sortedHashMap = new LinkedHashMap();
       for (Iterator it = list.iterator(); it.hasNext();) {
              Map.Entry entry = (Map.Entry) it.next();
              sortedHashMap.put(entry.getKey(), entry.getValue());
       }
        return sortedHashMap;
    }

    //this method is used for baseline tagger
    public void createWordTagCountMap() {
            for (Sentence sentence : sentenceList) {
            for (int i = 0; i < sentence.getWords().size(); i++) {

                Word word = sentence.getWords().get(i);

                Tag tag = sentence.getTags().get(i);

                if (wordTagCountMap.containsKey(word)) {

                    Map<Tag, Integer> map = wordTagCountMap.get(word);

                    if (map.containsKey(tag)) {

                        map.put(tag, map.get(tag) + 1);

                    } else {

                        map.put(tag, 1);
                    }

                    wordTagCountMap.put(word, map);

                } else {

                    Map<Tag, Integer> map = new HashMap<Tag, Integer>();

                    map.put(tag, 1);

                    wordTagCountMap.put(word, map);
                }
            }
        }
    }

    private void createPrevActivityMap(Map<Word, Map<Tag, Integer>> wordTagCountMap) {

                for(Map.Entry<Word,Map<Tag,Integer>> entry : wordTagCountMap.entrySet())
                {
                        Word word = entry.getKey();
                        Map<Tag, Integer> map = wordTagCountMap.get(word);
                        //System.out.println(word.toString()+" "+map);
                        for(Map.Entry<Tag,Integer> entry1:map.entrySet())
                        {
                            Tag tag=entry1.getKey();
                            if(tag.toString().contains("prev")) {
                            prevActivities.put(word.toString(),entry1.getValue());
                        }
                        }
                }
  }

    private void createNextActivityMap(Map<Word,Map<Tag,Integer>> wordTagCountMap) {
                for(Map.Entry<Word,Map<Tag,Integer>> entry : wordTagCountMap.entrySet())
                {
                        Word word = entry.getKey();
                        Map<Tag, Integer> map = wordTagCountMap.get(word);
                        //System.out.println(word.toString()+" "+map);
                        for(Map.Entry<Tag,Integer> entry1:map.entrySet())
                        {
                            Tag tag=entry1.getKey();
                            if(tag.toString().contains("next")) {
                            nextActivities.put(word.toString(),entry1.getValue());
                        }
                        }

                }
                //System.out.println(nextActivities);
    }
    private void createParticipantMap(Map<Word,Map<Tag,Integer>> wordTagCountMap) {

                for(Map.Entry<Word,Map<Tag,Integer>> entry : wordTagCountMap.entrySet())
                {
                    Word word = entry.getKey();
                    Map<Tag, Integer> map = wordTagCountMap.get(word);
                        //System.out.println(word.toString()+" "+map);
                        for(Map.Entry<Tag,Integer> entry1:map.entrySet())
                        {
                            Tag tag=entry1.getKey();
                            if(tag.toString().contains("part")) {
                            participants.put(word.toString(),entry1.getValue());
                        }
                        }
                }
    }

    private void getSimilarActivity() {
       for(Map.Entry<Word,Map<Tag,Integer>> entry : wordTagCountMap.entrySet())
        {
                    Word word = entry.getKey();
                        if (wordTagCountMap.containsKey(word)) {

                          Map<Tag, Integer> map = wordTagCountMap.get(word);
                        //System.out.println(word.toString()+" "+map);
                        for(Map.Entry<Tag,Integer> entry1:map.entrySet())
                        {
                            Tag tag=entry1.getKey();
                            if(tag.toString().contains("curr")) {
                            similarActivity.add(word.toString());
                        }
                        }
                        }
                }
        }


    public Tag retrieveMostFreqTagForWord(Word word) {

        if (wordTagCountMap.containsKey(word)) {

            Map<Tag, Integer> map = wordTagCountMap.get(word);

            ValueComparator valueComparator = new ValueComparator(map);

            TreeMap<Tag, Integer> treeMap = new TreeMap<Tag, Integer>(valueComparator);

            treeMap.putAll(map);

            return treeMap.firstKey();

        } else {

            return new Tag("NN");
        }


    }

    public Activity createGeniaTrainObject(String actName) {

        GeniaData trainData = new GeniaData();
        Activity act=new Activity();

        try
        {
            String currActivity1=actName;
            String fPath="C:\\Users\\Chirag\\Desktop\\src\\src\\Output_files\\";
            String fName=fPath+actName+".txt";
            String fPath1="C:\\Users\\Chirag\\Desktop\\src\\src\\output_file_1.txt";
            String fPath2="C:\\Users\\Chirag\\Desktop\\src\\src\\KB\\Output.txt";
            System.out.println(fName);
            PrintWriter writer1= new PrintWriter(new BufferedWriter(new FileWriter(fPath1, true)));
            PrintWriter writer2= new PrintWriter(new BufferedWriter(new FileWriter(fPath2, true)));
            //System.out.println(fName);
             trainData.readGeniaFile(fName);
             trainData.createTagMap();
             trainData.createWordTagCountMap();
              System.out.println(trainData.wordTagCountMap);
              trainData.createPrevActivityMap(trainData.wordTagCountMap);
              trainData.prevActivities=trainData.sortByValues(trainData.prevActivities);
              trainData.createNextActivityMap(trainData.wordTagCountMap);
              trainData.nextActivities=trainData.sortByValues(trainData.nextActivities);
              trainData.createParticipantMap(trainData.wordTagCountMap);
              trainData.participants=trainData.sortByValues(trainData.participants);
              trainData.getSimilarActivity();
              //if(trainData.similarActivity != null)
              //trainData.similarActivity.remove(trainData.similarActivity.indexOf(actName));
              ArrayList<String> temp = new ArrayList<String>(trainData.prevActivities.keySet());
              ArrayList<String> temp1 = new ArrayList<String>(trainData.nextActivities.keySet());

              int i=0;
              for(i=0;i<temp.size();i++)
              {
                  writer1.println(temp.get(i)+"/"+"curr");
                  writer1.println(actName+"/"+"next");
                  writer1.println("======================================================");
              }
              for(i=0;i<temp1.size();i++)
              {
                  writer1.println(actName+"/"+"prev");
                  writer1.println(temp1.get(i)+"/"+"curr");
                  writer1.println("======================================================");
              }

              trainData.prevActivities=getThree(trainData.prevActivities);
              trainData.nextActivities=getThree(trainData.nextActivities);
              temp = new ArrayList<String>(trainData.prevActivities.keySet());
              temp1 = new ArrayList<String>(trainData.nextActivities.keySet());
              ArrayList<String> temp2=new ArrayList<String>(trainData.participants.keySet());

              act.next=temp;
              act.prev=temp1;
              act.name=actName;
              for(i=0;i<trainData.participants.size();i++)
                {
                if(i==5)
                    break;
                else
                act.participant.add(temp2.get(i));
                }
                for(i=0;i<trainData.similarActivity.size();i++)
                {
                if(i==5)
                    break;
                else
                act.similarActivity.add(trainData.similarActivity.get(i));
                }
                writer2.println(act.name+"/curr");
                for(i=0;i<act.similarActivity.size();i++)
                    writer2.println(act.similarActivity.get(i)+"/simi");
                for(i=0;i<act.next.size();i++)
                    writer2.println(act.next.get(i)+"/next");
                for(i=0;i<act.prev.size();i++)
                    writer2.println(act.prev.get(i)+"/prev");
                for(i=0;i<act.participant.size();i++)
                        writer2.println(act.participant.get(i)+"/part");
              writer2.println("======================================================");
              //writer2.newLine();
              trainData.prevActivities.clear();
              trainData.nextActivities.clear();
              trainData.participants.clear();
              trainData.similarActivity.clear();
              trainData.wordTagCountMap.clear();
              trainData.tagMap.clear();
              writer1.close();
              writer2.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
            return act;
         }

    public GeniaData createGeniaTestObject() {

        GeniaData testData = new GeniaData();
        return testData;
    }

    private Map<String, Integer> getThree(Map<String, Integer> Activities) {

        int i=Activities.size();
              Iterator it1=Activities.entrySet().iterator();
              while(it1.hasNext())
              {
                   Map.Entry pair = (Map.Entry)it1.next();

                    if(i<=3)
                        break;
                  else
                  {
                      i--;
                  }
                    it1.remove();
              }
              return Activities;
    }


}
