/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final SET<Integer> set;
    private final int[] distTo;


    public BFS(Digraph G, Iterable<Integer> v) {
        set = new SET<Integer>();
        distTo = new int[G.V()];
        for (int i = 0; i < G.V(); i++) distTo[i] = INFINITY;
        bfs(G, v);
    }

    private void bfs(Digraph G, Iterable<Integer> v) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : v) {
            q.enqueue(s);
            distTo[s] = 0;
            set.add(s);
        }
        while (!q.isEmpty()) {
            int s = q.dequeue();
            for (int p : G.adj(s)) {
                if (!set.contains(p)) {
                    set.add(p);
                    q.enqueue(p);
                    distTo[p] = distTo[s] + 1;
                }
            }
        }
    }

    public SET<Integer> set() {
        return set;
    }

    public int[] distTo() {
        return distTo;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SET<Integer> a = new SET<Integer>();
        a.add(1);
        a.add(2);
        a.add(3);
        BFS bfs = new BFS(G, a);
        int[] dist = bfs.distTo();
        for (int i = 0; i < dist.length; i++) StdOut.println(dist[i]);
    }
}
