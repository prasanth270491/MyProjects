import semantics.Compare;
import java.util.*;
import tagger.Activity;
//import tagger.HMM;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.io.*;
import java.io.File;
import java.util.HashMap;
public class Similarity_2 {

	/**
	 * @param args

	 */
	public static HashMap<String, Activity> activityMap = new HashMap<String, Activity>();
	public Activity getActivity1(String actName)
	{
	    Activity act=null;
	    System.out.println("Activity Name"+actName);
	    act=activityMap.get(actName);
	    System.out.println(act);
	    return act;
	}
	public void createMap()
	{
        String activityName;
        BufferedReader br=null;
        String fileName="C:\\Users\\Chirag\\Desktop\\src\\src\\KB\\Output.txt";
        try{
            String fileLine;
            br = new BufferedReader(new FileReader(fileName));
            int curPos=0;
            while ((fileLine = br.readLine()) != null) {
                Activity act = new Activity();

                    if(fileLine==null)
                        break;
                    do
                    {
                           System.out.println(fileLine);
                           String [] content=fileLine.split("/");
                           if(content[1].equals("prev"))
                                  act.prev.add(content[0]);
                           else if(content[1].equals("next"))
                                  act.next.add(content[0]);
                           else if(content[1].equals("curr"))
                           {
                               System.out.println(content[0]);
                               act.name=content[0];
                        }
                           else if(content[1].equals("simi"))
                               act.similarActivity.add(content[0]);
                           else if(content[1].equals("part"))
                               act.participant.add(content[0]);

                    }while(!(fileLine = br.readLine()).equals("======================================================"));

                    System.out.println(act.name);
                    activityMap.put(act.name,act);
            }

        }
        catch (IOException e) {
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

	public  Activity getActivity(String actName) {

    List<String> train_activities = new ArrayList<String>();
    List<String> train_activities_line = new ArrayList<String>();
    List<String> input_activities = new ArrayList<String>();
    Activity act;
    HashMap<String,Double> list1 = new HashMap<String,Double>();
    HMM hmm=new HMM();
    String fileName = "output_file_1.txt";


        String line = null;

        try {
            FileReader fileReader =
                new FileReader(fileName);

            BufferedReader bufferedReader =
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                    List<Entry<String,String>> pairList = new ArrayList<>();
                    do {
                            //System.out.println(line);
                        if(line!= null) {
                           if(line.equals("============================================")){ break;}
                           String s[] = line.split("\\/");
                           if(s.length >=2) {
                              if(s[0] != null && s[1] != null && !s[0].isEmpty() && !s[1].isEmpty()){
                                 Entry<String,String> entryValue = new SimpleEntry<String, String>(s[0].trim(),s[1].trim());
                                 pairList.add(entryValue);
                              }
                           }
                        }
                    }while((line = bufferedReader.readLine())!= null);
                String fileLine = "";
                String prevLine = "";
                String nextLine = "";
                String partLine = "";
                for (Entry<String, String> pair : pairList) {
                if(pair.getValue().equals("curr")){
                    fileLine += pair.getKey() + " | ";
                }
                }

                for (Entry<String, String> pair : pairList) {
                if(pair.getValue().equals("prev")){
                    if(prevLine == "")
                        prevLine += pair.getKey();
                    else
                        prevLine += "/" + pair.getKey();
                }

                }
                fileLine += prevLine + " | ";
                for (Entry<String, String> pair : pairList) {
                if(pair.getValue().equals("next")){
                    if(nextLine == "")
                        nextLine += pair.getKey();
                    else
                        nextLine += "/" + pair.getKey();
                }

                }
                fileLine += nextLine + " | ";
                for (Entry<String, String> pair : pairList) {

                if(pair.getValue().equals("part")){
                    if(partLine == "")
                        partLine += pair.getKey();
                    else
                        partLine +="/" + pair.getKey();
                }

                }
                fileLine += partLine + " | ";

                System.out.println(fileLine);
                   train_activities_line.add(fileLine);
                  // System.out.println("FILE LINE");
                  // System.out.println(fileLine);


            }

            // Always close files.
            bufferedReader.close();
        }catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" +
                fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        String inputFileName = "test_activities.txt";

        input_activities.add(actName.trim());


        try {
           // PrintWriter writer = new PrintWriter("output_file_2.txt", "UTF-8");
            PrintWriter writer1 = new PrintWriter("output_file_3.txt", "UTF-8");
            String path="Output_files\\";
            File file = new File(path);
            File[] files = file.listFiles();
            for (File f:files)
            {if (f.isFile())
            {
                f.delete();
            } }
            for(int k=0;k<input_activities.size();k++)
            {
                String b = input_activities.get(k);
                String file_name = "Output_files/" + b + ".txt";
                PrintWriter file_writer = new PrintWriter(file_name, "UTF-8");
                if(b!= null && !b.isEmpty())
                {
                for(int i=0;i<train_activities_line.size();i++) {
                    //System.out.println(train_activities_line.get(i));
                    String params[] = train_activities_line.get(i).split("\\|");
                    if(params[0] != null && !params[0].isEmpty())
                    {
                       // System.out.println("Activity : " + params[0]);
                        Compare C = new Compare(params[0],b);
                        Double result = C.getResult();
                        list1.put(train_activities_line.get(i), result);
                   }
                }

                List list = new LinkedList(list1.entrySet());
                // Defined Custom Comparator here
                Collections.sort(list, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        return -((Comparable) ((Map.Entry) (o1)).getValue())
                            .compareTo(((Map.Entry) (o2)).getValue());
                    }
                });
                HashMap sortedHashMap = new LinkedHashMap();
                for (Iterator it = list.iterator(); it.hasNext();) {
                    Map.Entry entry = (Map.Entry) it.next();
                    sortedHashMap.put(entry.getKey(), entry.getValue());
                }
                Set set2 = sortedHashMap.entrySet();
                Iterator iterator2 = set2.iterator();
                int count =0;

                ArrayList<String> prevAct = new ArrayList<String>();
                ArrayList<String> nextAct = new ArrayList<String>();
                ArrayList<String> partArr = new ArrayList<String>();
                ArrayList<String> similarAct = new ArrayList<String>();
                String currAct = b + "/curr";
                while(iterator2.hasNext() && count != -1)
                {

                    Map.Entry me2 = (Map.Entry)iterator2.next();
                    String key = me2.getKey().toString();
                    String params[] = key.split("\\|");


                    if(((Double)me2.getValue() >(Double)0.6))
                    {

                        if(params != null&& params[0] != null && !params[0].isEmpty())
                        {
                            String similar_act = params[0].trim();

                                System.out.println(" Similar : " + b + "    " +similar_act);
                                if(params[1] != null && !params[1].isEmpty()){
                                    String prev[] = params[1].split("\\/");
                                    for(int i=0;i<prev.length;i++) {
                                        String val = prev[i].trim();
                                        if(val != null && !val.isEmpty())
                                        {
                                            file_writer.println(val+"/prev");
                                            prevAct.add(val+"/prev");
                                            //System.out.println(prev[i] + "/prev");
                                        }
                                    }
                                }
                                file_writer.println(similar_act + "/curr");
                                if(params[2] != null && !params[2].isEmpty()) {
                                    String next[] = params[2].split("\\/");
                                    for(int i=0;i<next.length;i++) {
                                        String val = next[i].trim();
                                        if(val != null && !val.isEmpty())
                                        {
                                            file_writer.println(val+"/next");
                                            nextAct.add(val+"/next");
                                            //System.out.println(next[i]+"/next");
                                        }
                                    }
                                }
                                if(params[3] != null && !params[3].isEmpty()) {
                                    String part[] = params[3].split("\\/");
                                    for(int i=0;i<part.length;i++) {
                                        String val = part[i].trim();
                                        if(val != null && !val.isEmpty())
                                        {
                                            file_writer.println(val+"/part");

                                            partArr.add(val+"/part");
                                            //System.out.println(part[i]+"/part");
                                        }
                                    }
                                }

                                count++;
                                file_writer.println("============================================");

                            //System.out.println(" Getting index " + input_activities.contains(similar_act));
                            if(!b.equals(similar_act))
                            {
                                similarAct.add(similar_act);
                            }
                            //input_activities.removeAll(Collections.singleton(similar_act));
                            //System.out.println(" Size " + input_activities.size());
                        }

                    } else {
                        count = -1;
                    }
                }


               // System.out.println(b+"|"+prev_act+"|"+next_act+"|"+participant);
                //System.out.println("PRINTING------------------------------");
                String printString = "";
                printString += b + "/";
               for(int i=0;i<similarAct.size();i++){
                    if(i==0) {
                        printString += similarAct.get(i);
                    } else {
                        printString += "/" + similarAct.get(i);
                    }
                    //System.out.println(prevAct.get(i));
               }
               writer1.println(printString);

               for(int i=0;i<prevAct.size();i++){
                  //writer.println(prevAct.get(i));
                //System.out.println(prevAct.get(i));
               }
             //  writer.println(currAct);
               for(int i=0;i<nextAct.size();i++){
                //writer.println(nextAct.get(i));
                //System.out.println(nextAct.get(i));
               }
               for(int i=0;i<partArr.size();i++){
                //  writer.println(partArr.get(i));
                //System.out.println(partArr.get(i));
               }
              //  writer.println("============================================");
              //  writer.println(b+"|"+prev_act+"|"+next_act+"|"+participant);
            }
            file_writer.close();

            }
            //writer.close();
            writer1.close();
        }catch(Exception ex){
               System.out.println(
                ex.getMessage());
        }
        act=hmm.getActivity(actName);
        activityMap.put(act.name,act);
        return act;
	}
}
