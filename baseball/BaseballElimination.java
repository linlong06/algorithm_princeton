/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final int n;
    private final LinearProbingHashST<String, Integer> st;
    private final int[] w;
    private final int[] loss;
    private final int[] r;
    private final int[][] g;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = Integer.parseInt(in.readLine());
        st = new LinearProbingHashST<String, Integer>();
        w = new int[n];
        loss = new int[n];
        r = new int[n];
        g = new int[n][n];
        int i = 0;
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] s = line.trim().split("\\s+");
            st.put(s[0], i);
            w[i] = Integer.parseInt(s[1]);
            loss[i] = Integer.parseInt(s[2]);
            r[i] = Integer.parseInt(s[3]);
            for (int k = 4; k < s.length; k++) {
                int j = k - 4;
                g[i][j] = Integer.parseInt(s[k]);
            }
            i++;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return st.keys();
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null || !st.contains(team))
            throw new IllegalArgumentException("Invalid Arguments!");
        return w[st.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null || !st.contains(team))
            throw new IllegalArgumentException("Invalid Arguments!");
        return loss[st.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null || !st.contains(team))
            throw new IllegalArgumentException("Invalid Arguments!");
        return r[st.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || !st.contains(team1) || team2 == null || !st.contains(team2))
            throw new IllegalArgumentException("Invalid Arguments!");
        return g[st.get(team1)][st.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null || !st.contains(team))
            throw new IllegalArgumentException("Invalid Arguments!");
        if (n == 1) return false;
        int x = st.get(team);
        for (int i = 0; i < n; i++) {
            if (w[x] + r[x] < w[i]) return true;
        }
        if (n == 2) return false;

        int V = n * (n - 1) / 2 + 2;
        FlowNetwork G = new FlowNetwork(V);
        int s = 0;
        int t = 1;

        for (int i = 0; i < n; i++) {
            if (i != x) G.addEdge(new FlowEdge(teamV(i, x), t, w[x] + r[x] - w[i]));
            for (int j = i + 1; j < n; j++) {
                if (i != x && j != x) {
                    G.addEdge(new FlowEdge(s, gameV(i, j, x), g[i][j]));
                    G.addEdge(new FlowEdge(gameV(i, j, x), teamV(i, x), Integer.MAX_VALUE));
                    G.addEdge(new FlowEdge(gameV(i, j, x), teamV(j, x), Integer.MAX_VALUE));
                }
            }
        }

        FordFulkerson ff = new FordFulkerson(G, s, t);
        for (int v = (n - 1) * (n - 2) / 2 + 2; v < V; v++) {
            if (ff.inCut(v)) return true;
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !st.contains(team))
            throw new IllegalArgumentException("Invalid Arguments!");
        if (!isEliminated(team)) return null;

        int x = st.get(team);
        Bag<String> listTeam = new Bag<String>();
        for (int i = 0; i < n; i++) {
            if (w[x] + r[x] < w[i]) {
                for (String key : st.keys()) {
                    if (st.get(key) == i) listTeam.add(key);
                }
            }
        }
        if (!listTeam.isEmpty()) return listTeam;

        int V = n * (n - 1) / 2 + 2;
        FlowNetwork G = new FlowNetwork(V);
        int s = 0;
        int t = 1;

        for (int i = 0; i < n; i++) {
            if (i != x) G.addEdge(new FlowEdge(teamV(i, x), t, w[x] + r[x] - w[i]));
            for (int j = i + 1; j < n; j++) {
                if (i != x && j != x) {
                    G.addEdge(new FlowEdge(s, gameV(i, j, x), g[i][j]));
                    G.addEdge(new FlowEdge(gameV(i, j, x), teamV(i, x), Integer.MAX_VALUE));
                    G.addEdge(new FlowEdge(gameV(i, j, x), teamV(j, x), Integer.MAX_VALUE));
                }
            }
        }

        FordFulkerson ff = new FordFulkerson(G, s, t);
        for (int v = (n - 1) * (n - 2) / 2 + 2; v < V; v++) {
            if (ff.inCut(v)) {
                int i = teamIndex(v, x);
                for (String key : st.keys()) {
                    if (st.get(key) == i) listTeam.add(key);
                }
            }
        }
        return listTeam;
    }

    // ganranteed i < j when this method is called
    private int gameV(int i, int j, int x) {
        int count = 2;
        for (int ii = 0; ii < i; ii++) {
            for (int jj = ii + 1; jj < n; jj++) {
                if (ii != x && jj != x) count++;
            }
        }
        for (int jj = i + 1; jj < j; jj++) {
            if (jj != x) count++;
        }
        return count;
    }

    private int teamV(int i, int x) {
        if (i < x) return (n - 1) * (n - 2) / 2 + 2 + i;
        else return (n - 1) * (n - 2) / 2 + 1 + i;
    }

    private int teamIndex(int teamV, int x) {
        int i = teamV - (n - 1) * (n - 2) / 2 - 2;
        if (i < x) return i;
        else return i + 1;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
