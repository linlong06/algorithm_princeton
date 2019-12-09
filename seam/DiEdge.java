/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class DiEdge {
    private final int v;
    private final int w;

    public DiEdge(int v, int w) {
        this.v = v;
        this.w = w;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public static void main(String[] args) {
        int width = 3;
        int height = 4;
        int V = width * height;
        for (int i = 0; i < V; i++) {
            StdOut.println("V = " + i + " Col: " + i % width + " Row: " + i / width);
        }

    }
}
