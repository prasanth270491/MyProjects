package opennlp;
import opennlp.tools.parser.*;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Chirag
 */

class Activity{
    String name;
    
    //ArrayList<String> prev, next,participant;
    
    ArrayList<Activity> prev,next;
    ArrayList<String> participant;   
    
    public Activity(String name) {
        this.name = name;
        prev = new ArrayList<>();
        next = new ArrayList<>();
        participant = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Activity{" + "name=" + name + ", participant=" + participant + '}';
    }

}

public class OpenNlp {
        
    
    static ArrayList<Activity> activityList = new ArrayList<Activity>();
    static HashMap<String, Activity> map = new HashMap<String, Activity>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{

  // always start with a model, a model is learned from training dataru
        
               //try
               //{    
                String[] Rule={"VP->VBG,NP,NN","VP->VBD,VP","VP->VBC","VP->VB,NP","VP->VBD,NP","VP->VBD","VP->VBN","NP->NNS","VP->VB,PP","VP->VB,NP","VB->VBD","VP->VBZ","VP->VBP,NP"};  
                InputStream is = new FileInputStream("en-sent.zip");
                SentenceModel model = new SentenceModel(is);
                SentenceDetectorME sdetector = new SentenceDetectorME(model);
                FileReader fname=new FileReader("act.txt");
                ArrayList<String> prev = new ArrayList<String>();
                String paragraph;
                BufferedReader reader=new BufferedReader(fname);
                while((paragraph=reader.readLine())!= null)    
                {
                    String sentences[] = sdetector.sentDetect(paragraph);
                    for(int j=0;j<sentences.length;j++)
                    {
                       //System.out.println(sentences[j]);
                        InputStream is2 = new FileInputStream("en-parser-chunking.zip");

                            ParserModel model1 = new ParserModel(is2);

                                Parser parser = ParserFactory.create(model1);

                                Parse topParses[] = ParserTool.parseLine(sentences[j], parser, 1);
                                Parse[] temp=null;
                                for (Parse p : topParses)
                                {
                                    ArrayList<String> part=null;
                                    p.show();
                                    ArrayList<String> curAct = traverse(p);
                                    if(curAct.size()>0)
                                        part= traverseParticipant(p);
                                    //String participant=null;
                                    for (int actNum = 0; actNum < curAct.size(); actNum++){ 
                                        String activity = curAct.get(actNum);
                                        if (activity != null){
                                            Activity act;
                                            if (map.containsKey(activity))
                                                act = map.get(activity);
                                            else
                                                act = new Activity(activity);
                                            if(actNum < part.size())
                                            {
                                                if(!act.participant.contains(part.get(actNum)))
                                                act.participant.add(part.get(actNum));
                                            
                                                if (actNum == curAct.size()-1){
                                                    int partNum = actNum+1;
                                                    while (partNum < part.size()){
                                                        
                                                                if(!act.participant.contains(part.get(partNum)))
                                                                 act.participant.add(part.get(partNum));
                                                                   
                                                                
                                                                partNum++;
                                                                }
                                                              
                                                            
                                                        
                                                    
                                                }
                                            }
                                            //System.out.println("Activity:"+activity);
                                            //System.out.println("Previous :");
                                            for (String preSentence : prev){
                                                 //System.out.println(preSentence);
                                                 Parse prevParses[] = ParserTool.parseLine(preSentence, parser, 1);
                                                 for (Parse pre : prevParses)
                                                 {
                                                     //p.show();
                                                    for (String activityPre : traverse(pre))
                                                        if (activityPre != null){
                                                            if(!act.prev.contains(map.get(activityPre)))
                                                                act.prev.add(map.get(activityPre));
                                                            if(!activityList.get(activityList.indexOf(map.get(activityPre))).next.contains(act))
                                                                activityList.get(activityList.indexOf(map.get(activityPre))).next.add(act);
                                                            //System.out.println("Prev Activity"+activityPre);
                                                        }
                                                 }
                                            }
                                            
                                            for (int i = 1; i <= actNum; i++){
                                                act.prev.add(activityList.get(activityList.size()-i));
                                                activityList.get(activityList.size()-i).next.add(act);
                                                //System.out.println(activityList.get(activityList.size()-i).name);
                                            }
                                            if (!map.containsKey(activity))
                                                activityList.add(act);
                                            map.put(activity, act);
                                            
                                            //System.out.println();
                                        }
                                    }
                                }
                                
                                if (prev.size() == 3)
                                    prev.remove(0);
                                prev.add(sentences[j]);
                                
                                is2.close();
                    }
                }
                    /*
                   for (Activity a : activityList)
                   {
                        System.out.print(a);
                        System.out.println("Previous");
                        for(int i=0;i<a.prev.size();i++)
                           System.out.print("   "+a.prev.get(i).name);
                           System.out.println("Next");
                        for(int i=0;i<a.next.size();i++)
                           System.out.print("   "+a.next.get(i).name);
                         System.out.println();
                        
                   }*/
                    //System.out.println(a);
                   wirteFile();
                is.close();
               /*}
               
               catch(Exception Ex)
               {
                   System.out.println(Ex);
               } */ 
    }          

    private static ArrayList<String> traverse(Parse head) {
       
        if(head==null)
            return null;
                 
        ArrayList<String> result = new ArrayList<String>();
        Parse[] temp1=head.getChildren();
                 
                    String[] rules= {"VP->VBG,NP,NN","VP->VB,PP","VP->VBD,NP","VP->VBD","VP->VBP","VP->VBZ,PP","VP->VBZ,NP","VP->VBG","VP->VB,NP","VP->VBG,PP","VP->VBD,NP","VP->VBZ,NP","VP->VB,NP","VP->VBG,NP","VP->VBG","VP->VBD","VP->VB"};
                    for(int rulelen=0;rulelen<rules.length;rulelen++)
                    {
                        
                        String[] temp= rules[rulelen].split("->");
                        if(head.getType().matches(temp[0]))
                        { 
                            String[] rule=null;
                            int i=0;
                            int j=0;
                            String activity=null;
                            if(temp[1].contains(",")) 
                            {
                                rule=temp[1].split(",");
                            }
                            else
                            {
                            rule=new String[1];
                            rule[0]=temp[1];
                            }
                       //System.out.println(temp[1]);
                            int firstMatch=0;
                            while(i<temp1.length)
                            {   if(j==rule.length)
                                {
                                    j=0;
                                    firstMatch=0;
                                    activity=null;
                                }
                                if(temp1[i].getType().matches(rule[j]))
                                {
                                    if(activity==null && firstMatch==0)
                                    {
                                        
                                        activity=temp1[i].toString();
                                        firstMatch=1;
                                    }
                                    else
                                    {
                                        
                                        activity=activity+" "+temp1[i].toString();
                                    }
                                    j++;
                                }
                                else
                                {
                                    j=0;
                                    activity=null;
                                    firstMatch=0;
                                }
                                i++;
                            }
                            if(activity != null)
                            {
                                //System.out.println("Activity:"+activity);
                                result.add(activity);
                            }
                        }
                    }
                                       
                    for(int j2=0;j2<temp1.length;j2++){
                        //System.out.println("Traverse....");
                        for (String res : traverse(temp1[j2]))
                            if (res != null && !result.contains(res))
                                result.add(res);
                    }
                    
                    
                    
                    return result;
    } 

    private static ArrayList<String> traverseParticipant(Parse head) {
         if(head==null)
            return null;
                 
        ArrayList<String> result = new ArrayList<String>();
        Parse[] temp1=head.getChildren();
                 
                    String[] rules= {"NP->NN","NP->NNP"};
                    for(int rulelen=0;rulelen<rules.length;rulelen++)
                    {
                        String[] temp= rules[rulelen].split("->");
                        if(head.getType().matches(temp[0]))
                        {
                            String[] rule=null;
                            int i=0;
                            int j=0;
                            String activity=null;
                            if(temp[1].contains(",")) 
                            {
                                rule=temp[1].split(",");
                            }
                            else
                            {
                            rule=new String[1];
                            rule[0]=temp[1];
                            }
                     //  System.out.println(temp[1]);
                            int firstMatch=0;
                            while(i<temp1.length)
                            {   if(j==rule.length)
                                {
                                    j=0;
                                    firstMatch=0;
                                    activity=null;
                                }
                                if(temp1[i].getType().matches(rule[j]))
                                {
                                    if(activity==null && firstMatch==0)
                                    {
                                        
                                        activity=temp1[i].toString();
                                        firstMatch=1;
                                    }
                                    else
                                    {
                                        
                                        activity=activity+" "+temp1[i].toString();
                                    }
                                    j++;
                                }
                                else
                                {
                                    j=0;
                                    activity=null;
                                    firstMatch=0;
                                }
                                i++;
                            }
                            if(activity != null)
                            {
                                //System.out.println("Parti:"+activity);
                                result.add(activity);
                            }
                        }
                    }
                                       
                    for(int j2=0;j2<temp1.length;j2++){
                        //System.out.println(temp1[j2].getType()+" "+temp1[j2].toString());
                        for (String res : traverseParticipant(temp1[j2]))
                            if (res != null && !result.contains(res))
                                result.add(res);
                    }
                    return result;
    }
    
    private static void wirteFile(){
        try {
            PrintWriter writer = new PrintWriter("9_Screenplay_1 .txt", "UTF-8");
            for (Activity act : activityList){
                writer.println("============================================");
                if (!act.prev.isEmpty()){
                    writer.println(act.prev.get(0).name+"/" +"prev");
                    for (int i = 1; i < act.prev.size(); i++)
                        writer.println( act.prev.get(i).name+"/" +"prev");
                }
                writer.print(act.name);
                writer.print("/");
                writer.print("curr");
                writer.println();
                if (!act.next.isEmpty()){
                    writer.println(act.next.get(0).name+"/" +"next");
                    for (int i = 1; i < act.next.size(); i++)
                        writer.println(act.next.get(i).name+"/" + "next");
                }
                if (!act.participant.isEmpty()){
                    writer.println(act.participant.get(0)+"/" + "part");
                    for (int i = 1; i < act.participant.size(); i++)
                        writer.println(act.participant.get(i)+"/" + "part");
                }
                //writer.println();
            }
            writer.println("============================================");
            writer.close();
        } catch (Exception e) {
        }
        
    }
}