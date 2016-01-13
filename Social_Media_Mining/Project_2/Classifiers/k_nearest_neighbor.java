/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;

public class k_nearest_neighbor {


    public static void main(String[] args) {
        // TODO code application logic here
            // PREPROCESSING THE DATA

        String fileNameRead = "snippet.txt";
        String ProcessedData = "ProcessedData.txt";
        String InvFrequency = "InverseFreq.txt";
        String testData = "testing.txt";
        String line = null;
        String newString = "";

        try {
            HashSet<String> hashtag = new HashSet<>();
            // FileReader reads text files in the default encoding.
            FileReader fileReader =  new FileReader(fileNameRead);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            FileWriter fileWriter = new FileWriter(ProcessedData);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


            while((line = bufferedReader.readLine()) != null) {
                newString = "";
                line = line.replaceAll("[0-9]"," ").trim();
                line = line.replaceAll("\"", " ").trim();
                line = line.replaceAll("\'", " ").trim();
                line = line.replaceAll("[.]","").trim();
                line = line.replaceAll("[.,!&:;-?`('-]","").trim();
              //  line = line.replaceAll("\\.", " ");
              //  line = line.replaceAll("\\-"," ").trim();

                String[] splitString = line.split("\\s{1,}");
                for(int i = 0;i<splitString.length;i++)
                {
                    if(splitString[i].length()>3 && splitString[i].contains("http")==false)
                    {
                        if(splitString[i].contains("#"))
                        {
                            hashtag.add(splitString[i].trim());
                        }
                        newString = newString +  splitString[i] + " ";
                    }

                }

                bufferedWriter.write( newString+"\n");
            }


            // Always close files.
            bufferedReader.close();
            bufferedWriter.close();

        }
        catch(Exception ex) {
            System.out.println("ERROR" + ex);
        }


        // CALCULATING INVERSE DOCUMENT FREQUENCY
        HashMap<String,InvDoc> wordCount = new HashMap<String,InvDoc>();

        line = "";
        int noOfTweets = 0;
        try
        {
           FileReader fileReader =  new FileReader(ProcessedData);
           BufferedReader bufferedReader = new BufferedReader(fileReader);


           while((line = bufferedReader.readLine()) != null)
           {
               HashSet<String> hs = new HashSet<String>();
               String[] words = line.split("\\s{1,}");
               for(int i =0; i<words.length;i++)
               {
                   String s = words[i];
                   if(s.contains("#")==false)
                   {
                       if(wordCount.containsKey(s))
                       {
                            InvDoc c  = wordCount.get(s);
                            c.count = c.count +1;
                            wordCount.put(s,c);
                       }
                       else
                       {
                           InvDoc c = new InvDoc();
                           c.count = 1;
                           wordCount.put(s, c);
                       }
                       if(hs.contains(s)==false)
                       {
                           hs.add(s);
                           InvDoc l = wordCount.get(s);
                           l.linecount++;
                           wordCount.put(s, l);
                       }

                   }
               }

               noOfTweets++;
           }
               bufferedReader.close();

        }
        catch(Exception e)
        {
            System.out.println("ERROR" + e);
        }

      //  System.out.println(wordCount);
      //  System.out.println(noOfTweets);

    Double logTotalDoc = Math.log10(noOfTweets);

    HashMap<String,Double> inDocFreq = new HashMap<String,Double>();



           for (Map.Entry<String, InvDoc> entry : wordCount.entrySet()) {

                String word = entry.getKey();
                InvDoc ob = entry.getValue();
                int totalNoOfTimes = ob.count;
                int noOfTweetsContainWord = ob.linecount;

                Double invValue =  ( logTotalDoc - Math.log10(noOfTweetsContainWord));
                inDocFreq.put(word, invValue);

            }

           try
           {
               FileWriter fileWriter = new FileWriter(InvFrequency);
               BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

               for (Map.Entry<String, Double> entry : inDocFreq.entrySet()) {
                   String s = entry.getKey()+"   "+ entry.getValue();
                   bufferedWriter.write(s+"\n");
               }
           }
           catch(Exception e)
           {
               System.out.println(e);
           }

           // COPYING THE PROCESSED DATA FILE TO LIST<STRING>
            List<String> ProcessData = new ArrayList<>();
           try
           {
               FileReader fileReader =  new FileReader(ProcessedData);
               BufferedReader bufferedReader = new BufferedReader(fileReader);


               while((line = bufferedReader.readLine()) != null)
               {
                   ProcessData.add(line);
               }

           }
           catch(Exception e)
           {
               System.out.println(e);
           }


           int total_prediction_count = 0, correct_prediction_count = 0;
           HashSet<String> test_tweets = new HashSet<String>();

           try
           {
              FileReader fileReader =  new FileReader(testData);
              BufferedReader bufferedReader = new BufferedReader(fileReader);

              while((line = bufferedReader.readLine()) != null)
              {
                  test_tweets.add(line.trim());
              }
                  bufferedReader.close();
           }
           catch(Exception e)
           {
               System.out.println("ERROR" + e);
           }

          Iterator tw_iterator = test_tweets.iterator();

          int cnt = 0;
          while(tw_iterator.hasNext() && cnt<10000)
          {
          cnt++;
          //String tw = "1192592090	RT @dworldisnow: #news #bbc #cnn News19 new results for People NewsÂ Mindy McCready Is DeadPeople MagazineAngela Weiss/Getty. Coun... htt ...	0	0";
          String tw = (String)tw_iterator.next();
          System.out.println(" Test Tweet   " + tw);

           // FInd THE MAXIMUM THREE WORDS
          TreeMap<String,Double> treemap = new TreeMap<>();

           String[] testwords = tw.split(" ");

           for(int i = 0;i<testwords.length;i++)
           {
               if(inDocFreq.containsKey(testwords[i]))
               {
                   Double val = inDocFreq.get(testwords[i]);
                   if(treemap.size()<3)
                   {
                       treemap.put(testwords[i],val);

                   }
                   else
                   {
                       if(val>treemap.firstEntry().getValue())
                       {
                           String s = treemap.firstEntry().getKey();
                           treemap.remove(s);
                           treemap.put(testwords[i],val);
                       }
                   }
               }
           }
           //System.out.println(treemap);


           List<String> chkWords = new ArrayList<>(treemap.keySet());
           List<String> toConsider = new ArrayList<String>();
           for(int i=0;i<ProcessData.size();i++)
           {
               int containsChkWord = 1;
               for(int j=0; j< chkWords.size(); j++) {
                    if(!(ProcessData.get(i).contains(chkWords.get(j))))
                    {
                         containsChkWord = 0;
                    }
               }
               if(containsChkWord == 1)
               {
                    toConsider.add(ProcessData.get(i));
               }
               if(toConsider.size() > 3000)
               {
                    break;
               }
           }
           if(toConsider.size() < 3000)
           {
                for(int i=0;i<ProcessData.size();i++)
                {
                    int count = 0;
                    for(int j=0; j< chkWords.size(); j++) {
                        if(!(ProcessData.get(i).contains(chkWords.get(j))))
                        {
                           count++;
                        }
                    }
                    if(count == 2 && !(toConsider.contains(ProcessData.get(i))))
                    {
                        toConsider.add(ProcessData.get(i));
                    }
                    if(toConsider.size() > 3000)
                    {
                        break;
                    }
                }
           }

           if(toConsider.size() <3000)
           {
                for(int i=0;i<ProcessData.size();i++)
                {
                    int count = 0;
                    for(int j=0; j< chkWords.size(); j++) {
                        if(!(ProcessData.get(i).contains(chkWords.get(j))))
                        {
                           count++;
                        }
                    }
                    if(count == 1 && !(toConsider.contains(ProcessData.get(i))))
                    {
                        toConsider.add(ProcessData.get(i));
                    }
                    if(toConsider.size() > 3000)
                    {
                        break;
                    }
                }
           }

           List<String> biglist = new ArrayList<>();
           TreeMap<String,Double> treemap2 = new TreeMap<>();

           for(int m=0;m<toConsider.size();m++)
           {
               String[] testword = toConsider.get(m).split(" ");
               for(int i = 0;i<testword.length;i++)
                {
               if(inDocFreq.containsKey(testword[i]))
               {
                   Double val = inDocFreq.get(testword[i]);
                   if(treemap2.size()<3)
                   {
                       treemap2.put(testword[i],val);

                   }
                   else
                   {
                       if(treemap != null && treemap.firstEntry()!= null && val>treemap.firstEntry().getValue())
                       {
                           String s = treemap2.firstEntry().getKey();
                           treemap2.remove(s);
                           treemap2.put(testword[i],val);
                       }
                   }
                }
                }
               for(int z = 0; z<treemap2.size();z++)
               {
                   biglist.add(treemap2.firstKey());
                   treemap2.remove(treemap2.firstEntry().getKey());
               }
           }

          // System.out.println(biglist.size() + " biglist ");

           HashMap<String,Double> tcor = new HashMap<>();

           for(int i = 0; i< biglist.size();i++)
           {
               int avg = 0;
               int cohash = 0;
               String s = biglist.get(i);
               for(int j =0; j<toConsider.size();j++)
               {
                   if(toConsider.get(j).contains(s))
                   {
                       String[] word = s.split("\\s{1,}");
                       avg = avg + word.length;
                       for(int k = 0;k<word.length;k++)
                       {
                           if(word[k].contains("#"))
                           {
                               cohash = cohash + 1;
                           }
                       }
                   }


               }

               Double t = ((1*.1*10/avg) + (1*.1*10/cohash))/2;
               tcor.put(s, t);


           }
           HashMap<String,Double> neighbor = new HashMap<>();
           int weight;
           for(int i =0;i<toConsider.size();i++)
           {
               Double score = 0.0;
               String[] tweetwords = toConsider.get(i).split(" ");
               for(int j = 0;j<tweetwords.length;j++)
               {
                   weight = 1;
                   if(tcor.containsKey(tweetwords[j]))
                   {
                       if(tweetwords[j].contains("@"))
                       {
                           weight = 3;
                       }
                      score = score + (tcor.get(tweetwords[j])* tweetwords[j].length() * weight) ;
                   }
               }
               neighbor.put(toConsider.get(i), score);
           }

           List list = new LinkedList(neighbor.entrySet());

           Collections.sort(list, new Comparator() {

               public int compare(Object o1, Object o2) {

                   return -((Comparable) ((Map.Entry) (o1)).getValue())

                       .compareTo(((Map.Entry) (o2)).getValue());

               }

           });



           // Top 10 tweets
           List<String> topten = new ArrayList<>();
           int count = 0;
           for (int i = 0;i<list.size();i++) {
               if(count>9)
               {
                   break;
               }
               Map.Entry entry = (Map.Entry)list.get(i);
               topten.add((String)entry.getKey());
               count++;
	}


           HashMap<String,Integer> getHighest = new HashMap<>();
           for(int i = 0;i<topten.size();i++)
           {
               String[] word = topten.get(i).split("\\s{1,}");
               for(int j =0;j<word.length;j++)
               {
                   if(word[j].contains("#"))
                   {
                       if(getHighest.containsKey(word[j]))
                       {
                           int c = getHighest.get(word[j]);
                           getHighest.put(word[j], c+1);
                       }
                       else
                       {
                           getHighest.put(word[j], 1);
                       }
                   }
               }
           }

           List list2 = new LinkedList(getHighest.entrySet());

           Collections.sort(list2, new Comparator() {

               public int compare(Object o1, Object o2) {

                   return -((Comparable) ((Map.Entry) (o1)).getValue())

                       .compareTo(((Map.Entry) (o2)).getValue());

               }

           });


           HashMap sortedHashMap = new LinkedHashMap();
           int count1 = 0;
           for (Iterator it = list2.iterator(); it.hasNext() && count1 <2; count1++) {

               Map.Entry entry = (Map.Entry) it.next();

               sortedHashMap.put(entry.getKey(), entry.getValue());

           }

           HashSet<String> tw_hashtags = new HashSet<>();
           String[] tw_words = tw.split("\\s{1,}");
           for(int j=0;j<tw_words.length;j++)
           {
               if(tw_words[j].charAt(0)=='#')
               {
                   tw_hashtags.add(tw_words[j].trim());
               }
           }

           Set set2 = sortedHashMap.entrySet();

           Iterator iterator2 = set2.iterator();

           int predicted = 0;
           while(iterator2.hasNext())
           {

               Map.Entry me2 = (Map.Entry)iterator2.next();

               //System.out.println(me2.getKey() + "   " + me2.getKey().toString().length()+"    Prob:    " + me2.getValue().toString());

               if(tw_hashtags.contains(me2.getKey()))
               {

                    predicted = 1;
                    break;
               }

           }
           if(predicted == 1)
           {
               correct_prediction_count++;
           }

           total_prediction_count++;

           System.out.println(" Correctly Prediction Count:   " + (correct_prediction_count));

           System.out.println(" Total predicted Count:  " + (total_prediction_count));

           System.out.println(" Accuracy:  " + ((float)correct_prediction_count/total_prediction_count) * 100.0);


        }
           System.out.println(" Correct Prediction Count:   " + (correct_prediction_count));

           System.out.println(" Count of total predicted tweets:  " + (total_prediction_count));

           System.out.println(" Accuracy:  " + ((float)correct_prediction_count/total_prediction_count) * 100.0);


    }

}
