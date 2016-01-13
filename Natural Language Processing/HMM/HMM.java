

import tagger.Activity;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import tagger.algo.Baseline;
import tagger.algo.Viterbi;
import tagger.algo.GeniaData;
import tagger.*;
/**
 * Created with IntelliJ IDEA.
 * User: ferhataydin
 * Date: 13/12/13
 * Time: 02:21
 * To change this template use File | Settings | File Templates.
 */
public class HMM {

    public Activity getActivity(String name) {
        Activity act=new Activity();
        Viterbi viterbi = new Viterbi();
        System.out.println("Start Hmm"+name);
        act=viterbi.run(name);
        return act;
    }

}
