/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph G;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Invalid Arguments!");
        }
        this.G = G;
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("Input Vertext Out of Bound!");
        }

        SET<Integer> setv = new SET<Integer>();
        SET<Integer> setw = new SET<Integer>();
        setv.add(v);
        setw.add(w);
        return length(setv, setw);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("Input Vertext Out of Bound!");
        }

        SET<Integer> setv = new SET<Integer>();
        SET<Integer> setw = new SET<Integer>();
        setv.add(v);
        setw.add(w);
        return ancestor(setv, setw);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        for (int s : v) {
            if (s < 0 || s >= G.V())
                throw new IllegalArgumentException("Input Vertext Out of Bound!");
        }
        for (int s : w) {
            if (s < 0 || s >= G.V())
                throw new IllegalArgumentException("Input Vertext Out of Bound!");
        }

        BFS bfs1 = new BFS(G, v);
        BFS bfs2 = new BFS(G, w);
        SET<Integer> set1 = bfs1.set();
        SET<Integer> set2 = bfs2.set();
        int[] dist1 = bfs1.distTo();
        int[] dist2 = bfs2.distTo();

        int minDistance = INFINITY;
        int ancestor = -1;
        for (int s : set1) {
            if (set2.contains(s) && dist1[s] + dist2[s] < minDistance) {
                ancestor = s;
                minDistance = dist1[s] + dist2[s];
            }
        }
        if (ancestor == -1) return -1;
        else return minDistance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for (int s : v) {
            if (s < 0 || s >= G.V())
                throw new IllegalArgumentException("Input Vertext Out of Bound!");
        }
        for (int s : w) {
            if (s < 0 || s >= G.V())
                throw new IllegalArgumentException("Input Vertext Out of Bound!");
        }

        BFS bfs1 = new BFS(G, v);
        BFS bfs2 = new BFS(G, w);
        SET<Integer> set1 = bfs1.set();
        SET<Integer> set2 = bfs2.set();
        int[] dist1 = bfs1.distTo();
        int[] dist2 = bfs2.distTo();

        int minDistance = INFINITY;
        int ancestor = -1;
        for (int s : set1) {
            if (set2.contains(s) && dist1[s] + dist2[s] < minDistance) {
                ancestor = s;
                minDistance = dist1[s] + dist2[s];
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
