/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Topological;

public class SP {
    private final double[] distTo;
    private final DirectedEdge[] edgeTo;

    public SP(VertexWeightedDAG vG, int s) {
        int V = vG.V();
        EdgeWeightedDigraph eG = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++) {
            for (DiEdge e : vG.adj(v)) {
                int w = e.to();
                double weight = vG.weight(v) + vG.weight(w);
                DirectedEdge de = new DirectedEdge(v, w, weight);
                eG.addEdge(de);
            }
        }

        distTo = new double[V];
        edgeTo = new DirectedEdge[V];
        for (int v = 0; v < V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;

        Topological tp = new Topological(eG);
        for (int v : tp.order()) {
            for (DirectedEdge e : eG.adj(v)) {
                relax(e);
            }
        }
    }

    public double distTo(int v) {
        return distTo[v];
    }

    Iterable<DirectedEdge> pathTo(int v) {
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    private void relax(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }
    }

    public static void main(String[] args) {
        // leaves empty intentionally
    }
}
