/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Map;

public class WordNet {
    private Digraph lexicon;
    private Map<String, Bag<Integer>> nouns;
    private Map<Integer, String> synsetidx;
    private SAP sap;

    // adj list
    public WordNet (String synsets, String hypernyms){
        if (synsets == null) throw new IllegalArgumentException();
        if (hypernyms == null) throw new IllegalArgumentException();
        nouns = new HashMap<>();
        synsetidx= new HashMap<>();
        int count = readSynsets(synsets);
        lexicon = new Digraph(count);
        readSynHyp(hypernyms);
        DirectedCycle cycornot = new DirectedCycle(lexicon);
        if (cycornot.hasCycle())
            throw new IllegalArgumentException();
        sap = new SAP(lexicon);
        int roots = 0;
        for (int i = 0; i < lexicon.V(); i++){
            if (lexicon.outdegree(i) == 0)
                roots ++;
        }
        if (roots != 1)
            throw new IllegalArgumentException();
    }

    private int readSynsets(String synsets){
        In in = new In (synsets);
        int count = 0;
        while (in.hasNextLine()){
            String line = in.readLine();
            String[] words = line.split(",");
            String synsetnouns = words[1];
            synsetidx.put(count, synsetnouns);
            String [] synnouns = synsetnouns.split(" ");
            for (int i = 0; i < synnouns.length; i++){
                if (nouns.containsKey(synnouns[i]))
                    nouns.get(synnouns[i]).add(count);
                else {
                    Bag<Integer> tmp = new Bag<>();
                    tmp.add(count);
                    nouns.put(synnouns[i], tmp);
                }
            }
            count++;
        }
        return count;
    }

    private void readSynHyp (String hypernyms){
        In in = new In (hypernyms);
        while (in.hasNextLine()){
            String line = in.readLine();
            String[] numbers = line.split(",");
            int v = Integer.parseInt(numbers[0]);
            Bag<Integer> temp = new Bag<>();
            for (int i = 1; i < numbers.length; i++){
                int v2 = Integer.parseInt(numbers[i]);
                temp.add(v2);
                lexicon.addEdge(v, v2);
            }
        }
    }

    public Iterable<String> nouns(){
        return nouns.keySet();
    }

    public boolean isNoun(String word){
        if (word == null) throw new IllegalArgumentException();
        return nouns.containsKey(word);
    }

    public int distance(String nounA, String nounB){
        if (nounA == null) throw new IllegalArgumentException();
        if (nounB == null) throw new IllegalArgumentException();
        if (!nouns.containsKey(nounA)) throw new IllegalArgumentException();
        if (!nouns.containsKey(nounB)) throw new IllegalArgumentException();
        Bag<Integer> b1 = nouns.get(nounA);
        Bag<Integer> b2 = nouns.get(nounB);
        return sap.length(b1, b2);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null) throw new IllegalArgumentException();
        if (nounB == null) throw new IllegalArgumentException();
        if (!nouns.containsKey(nounA)) throw new IllegalArgumentException();
        if (!nouns.containsKey(nounB)) throw new IllegalArgumentException();
        int ancestor = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        return synsetidx.get(ancestor);
    }

    public static void main(String[] args) {
    }
}
