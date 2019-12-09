/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class WordNet {
    private final SET<String> nouns;
    private final ST<Integer, ArrayList<String>> st;
    private final Digraph G;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Invalid Arguments!");
        }
        In ins = new In(synsets);
        In inh = new In(hypernyms);
        nouns = new SET<String>();
        st = new ST<Integer, ArrayList<String>>();
        int V = 0;

        while (!ins.isEmpty()) {
            String line = ins.readLine();
            String[] tokens = line.split(",");
            int v = Integer.parseInt(tokens[0]);
            String[] words = tokens[1].split(" ");
            ArrayList<String> bagwords = new ArrayList<String>();
            for (String word : words) {
                bagwords.add(word);
                if (!nouns.contains(word)) nouns.add(word);
            }
            st.put(v, bagwords);
            V++;
        }

        G = new Digraph(V);
        while (!inh.isEmpty()) {
            String line = inh.readLine();
            String[] tokens = line.split(",");
            int v = Integer.parseInt(tokens[0]);
            for (int i = 1; i < tokens.length; i++) {
                int w = Integer.parseInt(tokens[i]);
                G.addEdge(v, w);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Invalid Arguments!");
        }
        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Invalid Arguments!");
        }
        SAP sap = new SAP(G);
        SET<Integer> A = new SET<Integer>();
        SET<Integer> B = new SET<Integer>();
        for (int i = 0; i < st.size(); i++) {
            if (st.get(i).contains(nounA)) A.add(i);
            if (st.get(i).contains(nounB)) B.add(i);
        }
        return sap.length(A, B);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Invalid Arguments!");
        }
        SAP sap = new SAP(G);
        SET<Integer> A = new SET<Integer>();
        SET<Integer> B = new SET<Integer>();
        for (int i = 0; i < st.size(); i++) {
            if (st.get(i).contains(nounA)) A.add(i);
            if (st.get(i).contains(nounB)) B.add(i);
        }
        int v = sap.ancestor(A, B);
        ArrayList<String> bagwords = st.get(v);
        String s = "";
        for (String word : bagwords) {
            s = s + word + " ";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wn = new WordNet(synsets, hypernyms);
        for (String noun : wn.nouns()) StdOut.println(noun);
        StdOut.println(wn.isNoun("a"));
        StdOut.println(wn.distance("a", "b"));
        StdOut.println(wn.sap("a", "b"));
    }
}
