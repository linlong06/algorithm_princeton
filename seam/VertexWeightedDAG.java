/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;

public class VertexWeightedDAG {
    private final int V;
    private final double[] weight;
    private final Bag<DiEdge>[] adj;

    public VertexWeightedDAG(int V, double[] weight) {
        this.V = V;
        this.weight = new double[V];
        adj = (Bag<DiEdge>[]) new Bag[V];
        for (int i = 0; i < V; i++) {
            this.weight[i] = weight[i];
            adj[i] = new Bag<DiEdge>();
        }
    }

    public double weight(int v) {
        return weight[v];
    }

    public void addEdge(DiEdge e) {
        int v = e.from();
        adj[v].add(e);
    }

    public Iterable<DiEdge> adj(int v) {
        return adj[v];
    }

    public int V() {
        return V;
    }

    public static void main(String[] args) {
        // leaves empty intentionally
    }
}
