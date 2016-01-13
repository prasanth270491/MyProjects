import semantics.Compare;
import java.util.*;
import java.io.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class Similarity_1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

    List<String> train_activities = new ArrayList<String>();
    List<String> train_activities_line = new ArrayList<String>();
    List<String> input_activities = new ArrayList<String>();
    List<String> input_string = new ArrayList<String>();

    HashMap<String,Double> list1 = new HashMap<String,Double>();

    String fileName = "activities.txt";


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
        String inputFileName = "activities.txt";

        // This will reference one line at a time
        String inputLine = null;

        try {
            FileReader inputFileReader =
                new FileReader(inputFileName);

            BufferedReader inputBufferedReader =
                new BufferedReader(inputFileReader);

            while((inputLine = inputBufferedReader.readLine()) != null) {
                    List<Entry<String,String>> pairList = new ArrayList<>();

                    do {
                            //System.out.println(line);
                        if(inputLine!= null) {
                           if(inputLine.equals("============================================")){ break;}
                           String s[] = inputLine.split("\\/");
                           if(s.length >=2) {
                              if(s[0] != null && s[1] != null && !s[0].isEmpty() && !s[1].isEmpty()){
                                 Entry<String,String> entryValue = new SimpleEntry<String, String>(s[0].trim(),s[1].trim());
                                 pairList.add(entryValue);
                              }
                           }
                        }
                    }while((inputLine = inputBufferedReader.readLine())!= null);

                String fileLine = "";
                String prevLine = "";
                String nextLine = "";
                String partLine = "";
                String activityName = "";
                for (Entry<String, String> pair : pairList) {
                if(pair.getValue().equals("curr")){
                    fileLine += pair.getKey() + " | ";
                    activityName = pair.getKey().toString();
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

                        input_activities.add(activityName.trim());
                        input_string.add(fileLine);
            }

            // Always close files.
            inputBufferedReader.close();
        }catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" +
            inputFileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '"+ inputFileName + "'");
        }

        try {
            PrintWriter writer = new PrintWriter("output_file_1.txt", "UTF-8");
            //System.out.println(input_activities.size());
            for(int k=0;k<input_activities.size();k++)
            {
                //String input = input_string.get(k);
                String b = input_activities.get(k);
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

                while(iterator2.hasNext() && count <= 3 && count != -1)
                {

                    Map.Entry me2 = (Map.Entry)iterator2.next();
                    //System.out.println(me2.getKey()+" " + me2.getValue().toString());
                    String key = me2.getKey().toString();
                    String params[] = key.split("\\|");

                    if(((Double)me2.getValue() >(Double)0.5))
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
                                            prevAct.add(val+"/prev");
                                            //System.out.println(prev[i] + "/prev");
                                        }
                                    }
                                }

                                if(params[2] != null && !params[2].isEmpty()) {
                                    String next[] = params[2].split("\\/");
                                    for(int i=0;i<next.length;i++) {
                                        String val = next[i].trim();
                                        if(val != null && !val.isEmpty())
                                        {
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
                                            partArr.add(val+"/part");
                                            //System.out.println(part[i]+"/part");
                                        }
                                    }
                                }

                                count++;

                            //System.out.println(" Getting index " + input_activities.contains(similar_act));
                            if(!b.equals(similar_act))
                            {
                                similarAct.add(similar_act);
                            }
                            input_activities.removeAll(Collections.singleton(similar_act));
                            //System.out.println(" Size " + input_activities.size());
                        }

                    } else {
                        count = -1;
                    }
                }

               // System.out.println(b+"|"+prev_act+"|"+next_act+"|"+participant);
                //System.out.println("PRINTING------------------------------");

               for(int i=0;i<prevAct.size();i++){
                writer.println(prevAct.get(i));
                //System.out.println(prevAct.get(i));
               }
               writer.println(currAct);
               for(int i=0;i<nextAct.size();i++){
                writer.println(nextAct.get(i));
                //System.out.println(nextAct.get(i));
               }
               for(int i=0;i<partArr.size();i++){
                writer.println(partArr.get(i));
                //System.out.println(partArr.get(i));
               }
                writer.println("============================================");
              //  writer.println(b+"|"+prev_act+"|"+next_act+"|"+participant);
            }
            }

            writer.close();
        }catch(Exception ex){
               System.out.println(
                ex.getMessage());
        }
	}
}
