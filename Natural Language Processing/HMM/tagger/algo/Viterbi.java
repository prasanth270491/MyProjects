package tagger.algo;


import tagger.Activity;
import static com.oracle.jrockit.jfr.DataType.DOUBLE;
import static com.oracle.jrockit.jfr.DataType.STRING;
import tagger.domain.Constants;
import tagger.domain.Sentence;
import tagger.domain.Tag;
import tagger.*;
//import <default package>;
import java.util.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ferhataydin
 * Date: 12/12/13
 */

public class Viterbi {

    private int taggedCorrectly = 0;
    private int totalTagNumber = 0;
     Map<String,Double> prevActivity=new HashMap<>();
          Map<String,Double> nextActivity=new HashMap<>();
          Map<String,Double> part=new HashMap<>();
          String currActivity= null;
    HashMap<Tag,Double> Obs = new HashMap<Tag,Double>();
   // HashMap<,Double> Obs = new HashMap<Tag,Double>();
   // HashMap<Tag,Double> Obs = new HashMap<Tag,Double>();
    public Activity run(String act) {
    Activity act1=prepareViterbi(act);
    return act1;
    }

    private Activity prepareViterbi(String act) {

        GeniaData geniaTrainObj = new GeniaData();

        Activity act1 = geniaTrainObj.createGeniaTrainObject(act);

        return act1;
    }

    private void runViterbi(GeniaData geniaTestObj, HmmProb hmmProb, List<Tag> tagList) {

        try {

            PrintWriter writer = new PrintWriter(Constants.VITERBI_PATH_OUTPUT_FILE, "UTF-8");

           // System.out.println(Constants.VITERBI_PATH_OUTPUT_FILE + " is being generated..");

            for (Sentence sentence : geniaTestObj.getSentenceList()) {

                int sentenceLength = sentence.getWords().size();
                int tagListLength = tagList.size();

                Double[][] viterbiTable = new Double[tagListLength][sentenceLength];

                List<Tag> viterbiPath = new ArrayList<Tag>();

                // calculate first transition (first column in table)
                for (int t = 1; t < tagListLength; t++) {

                    Double tagTransitionProb = hmmProb.calculateTagTransitionProb(tagList.get(0), tagList.get(t));
                    Double wordTagLikelihoodProb = hmmProb.calculateWordTagLikelihoodProb(sentence.getWords().get(1), tagList.get(t));
                    viterbiTable[t][1] = tagTransitionProb * wordTagLikelihoodProb;
                      //System.out.println(wordTagLikelihoodProb.toString()+sentence.getWords().get(1)+tagList.get(t));


                }

                for (int w = 2; w < sentenceLength; w++) {

                    for (int t = 1; t < tagListLength; t++) {

                        Double[] probs = new Double[tagListLength];

                        for (int i = 1; i < tagListLength; i++) {

                            if (viterbiTable[i][w-1] != null) {

                                Double tagTransitionProb = hmmProb.calculateTagTransitionProb(tagList.get(i), tagList.get(t));

                                Double wordTagLikelihoodProb = hmmProb.calculateWordTagLikelihoodProb(sentence.getWords().get(w), tagList.get(t));
                                //Obs.put(tagList.get(t),wordTagLikelihoodProb);

                               // System.out.println(wordTagLikelihoodProb.toString()+sentence.getWords().get(w)+tagList.get(i)+tagList.get(t));
                                probs[i] = viterbiTable[i][w-1] * tagTransitionProb * wordTagLikelihoodProb;
                               // System.out.println(probs[i]);
                            } else {

                                probs[i] = 0.0;

                            }
                        }

                        int argmax = argmax(probs);

                        viterbiTable[t][w] = probs[argmax];
                    }
                }

                generateViterbiPath(tagList, sentenceLength, tagListLength, viterbiTable, viterbiPath, sentence, writer);

            }

            writer.close();

            //System.out.println(Constants.VITERBI_PATH_OUTPUT_FILE + " is generated..");

           // printAccuracy();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private void generateViterbiPath(List<Tag> tagList, int sentenceLength, int tagListLength, Double[][] viterbiTable, List<Tag> viterbiPath, Sentence sentence, PrintWriter writer) {
         // System.out.println("New Sentence");

        for (int w = 1; w < sentenceLength; w++) {

            Double[] probs = new Double[tagListLength];

            for (int t = 1; t < tagListLength; t++) {

                probs[t] = viterbiTable[t][w];
            }

            int index = argmax(probs);
            if(sentence.getTags().get(w).toString().matches("curr"))
                currActivity=sentence.getWords().get(w).toString();
            if(tagList.get(index).toString().matches("prev") && !sentence.getTags().get(w).toString().matches("curr") )
            {
                //currActivity=sentence.getWords().get(w).toString();
                prevActivity.put(sentence.getWords().get(w).toString(),probs[index]);
                //System.out.println(prevActivity);
            }
            else if(tagList.get(index).toString().matches("next") && !sentence.getTags().get(w).toString().matches("curr"))
            {
                nextActivity.put(sentence.getWords().get(w).toString(),probs[index]);
               // System.out.println(nextActivity);
            }
            else if(tagList.get(index).toString().matches("part") && !sentence.getTags().get(w).toString().matches("curr"))
            {
                part.put(sentence.getWords().get(w).toString(),probs[index]);
               // System.out.println(part);
            }
           // System.out.println(tagList.get(index).toString()+sentence.getWords().get(w));
            viterbiPath.add(tagList.get(index));

        }

        viterbiPath.add(0, new Tag(Constants.SENTENCE_START));

        //System.out.println(prevActivity);
        //System.out.println(nextActivity);
        //System.out.println(part);
        int n = prevActivity.size();
        prevActivity=sortByValues(prevActivity);
        nextActivity=sortByValues(nextActivity);
        part=sortByValues(part);
      /*  int l = part.size();
        Iterator it2 = part.entrySet().iterator();
        while (it2.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
           // System.out.println(pair.getKey() + " = " + pair.getValue());
            if(l==1)
                break;
             else
            {
                System.out.println(prevActivity);
                l--;
            }
            it2.remove();
        }*/
        //System.out.println("After Sort");
       // System.out.println(prevActivity);
      //  System.out.println(nextActivity);
        System.out.println("Activity:"+currActivity);

         printtop2prevactivities();
        printtop2nextactivities();
        printtop2partcipants();


        writer.println("Previous Activity: " + prevActivity);
       writer.println("Next Activity: " + sentence.getTags());
       writer.println("Participant: " + sentence.getTags());
       writer.println("vibertiPath        : " + viterbiPath);
        writer.println();

        calculateAccuracy(viterbiPath, sentence);

    }
    public void printtop2prevactivities()
    {
        System.out.println("Top 2 Previous Activity");
        int l = prevActivity.size();
        Iterator it1 = prevActivity.entrySet().iterator();
        while (it1.hasNext())
        {
            Map.Entry pair = (Map.Entry)it1.next();
           // System.out.println(pair.getKey() + " = " + pair.getValue());
            if(l==2)
                break;
             else
            {

                l--;
            }
            it1.remove();
        }
        System.out.println(prevActivity);
    }
     public void printtop2nextactivities()
    {
        System.out.println("Top 2 Next Activity");
        int k = nextActivity.size();
        Iterator it1 = nextActivity.entrySet().iterator();
        while (it1.hasNext())
        {
            Map.Entry pair = (Map.Entry)it1.next();
           // System.out.println(pair.getKey() + " = " + pair.getValue());
            if(k==2)
                break;
             else
            {

                k--;
            }
            it1.remove();
        }
        System.out.println(nextActivity);
    }
     public void printtop2partcipants()
    {
        System.out.println("Top 2 Particpants");
        int j = part.size();
        Iterator it3 = part.entrySet().iterator();
        while (it3.hasNext())
        {
            Map.Entry pair = (Map.Entry)it3.next();
           // System.out.println(pair.getKey() + " = " + pair.getValue());
            if(j==2)
                break;
             else
            {

                j--;
            }
            it3.remove();
        }
        System.out.println(part);
    }

    private void calculateAccuracy(List<Tag> viterbiPath, Sentence sentence) {

        for (int i = 0; i < viterbiPath.size(); i++) {

            if (viterbiPath.get(i).equals(sentence.getTags().get(i))) {

                taggedCorrectly++;
            }

            totalTagNumber++;
        }
    }

    private static Integer argmax(Double[] arr) {

        Double max = arr[1];

        Integer argmax = 1;

        for (int i=2; i<arr.length; i++) {

            if (arr[i] > max) {

                max = arr[i];
                argmax = i;

            }
        }

        return argmax;
    }

    private List<Tag> convertKeySetToList(HmmProb hmmProb) {

        Set set = hmmProb.getTagMap().keySet();

        List<Tag> tagList = new ArrayList<Tag>();

        tagList.addAll(set);

        tagList.remove(new Tag(Constants.SENTENCE_START));
        tagList.remove(new Tag(Constants.SENTENCE_END));

        tagList.add(0, new Tag(Constants.SENTENCE_START));
        tagList.add(tagList.size(), new Tag(Constants.SENTENCE_END));

        return tagList;
    }

    private Map<String, Double> sortByValues(Map<String, Double> map) {
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

}
