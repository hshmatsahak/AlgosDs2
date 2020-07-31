/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;
    private SAP sap;

    public Outcast(WordNet wordnet){
        wordNet = wordnet;

    }

    public String outcast(String[] nouns){
        String outcast = null;
        int maxdistance = 0;
        for (int i = 0; i < nouns.length; i++){
            int distance = 0;
            for (int j=0; j<nouns.length; j++){
                distance += wordNet.distance(nouns[i], nouns[j]);
            }
            if (distance > maxdistance){
                maxdistance = distance;
                outcast = nouns[i];
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
