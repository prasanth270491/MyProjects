1.
For running the opennlp.java file
We need to download apache-opennlp-1.5.3-bin zip file from OpenNlp site.
We need the jar files from the bin folder of apache-opennlp-1.5.3 which we get from extracting from the zip file
We also need en-parser-maxent models and en-sent models for training the OpenNlp parser and sentence detection.
We need to give the movie script file in .txt format to the code. 

2.
1) Compile and run Similarity_1.java to combine similar activities. activities.txt is the file which contains the activities      extracted from movie scripts. Similarity_1.java will run on activities.txt and combine the information of similar and duplicate activities into one activity. 

Input file: activities.txt
Output file: output_file_1.txt

2) Similarity_2.java runs on test_activities.txt and output_file_1.txt generated as output of step1. test_activities.txt contains the test activities for which we need to find the most probable previous,next activities using HMM. 

Output files: output_file_2.txt
              //OutputFile//activity_name.txt
 The first output file contains the merged information of similar activities for a test activity. The second file contains list of similar activities for an activity.




To run the java files,


download the zip file from here, and extract it.
http://sourceforge.net/projects/semantics/files/

and copy the folders category_dict, collocation_dict, net, semantics, WORDS from the extracted folder to the project source directory.
Also copy the java files, Similarity_1.java and Similarity_2.java and activities.txt
And then run Similarity_1.java followed by Similarity_2.java

3. For HMM we need to give one files as activtiy.txt. We update the KB file in KB folder
actInput.txt contains all the data which we get from similar activities and simInput.txt contains all the similar activities for a given activity.

4.

For running the system we need to run UI.java from command prompt.
