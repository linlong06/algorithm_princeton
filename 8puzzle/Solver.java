/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Solver {
    private final ArrayList<Node> solutionNodes;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Invalid Input!");
        }
        MinPQ<Node> pq = new MinPQ<Node>();
        MinPQ<Node> pqTwin = new MinPQ<Node>();
        Node initialNode = new Node(initial, 0, null);
        Node twinNode = new Node(initial.twin(), 0, null);
        pq.insert(initialNode);
        pqTwin.insert(twinNode);
        solutionNodes = new ArrayList<Node>();
        while (!pq.min().board.isGoal() && !pqTwin.min().board.isGoal()) {
            Node minNode = pq.delMin();
            Node minNodeTwin = pqTwin.delMin();
            solutionNodes.add(minNode);
            // add codes to deplete the two priority queues so the solution boards always have neighboring boards.
            for (Board board : minNode.board.neighbors()) {
                if (minNode.prev != null && board.equals(minNode.prev.board)) continue;
                Node newNode = new Node(board, minNode.moves + 1, minNode);
                pq.insert(newNode);
            }
            for (Board board : minNodeTwin.board.neighbors()) {
                if (minNodeTwin.prev != null && board.equals(minNodeTwin.prev.board)) continue;
                Node newNodeTwin = new Node(board, minNodeTwin.moves + 1, minNodeTwin);
                pqTwin.insert(newNodeTwin);
            }
        }
        solutionNodes.add(pq.min());
    }

    private class Node implements Comparable<Node> {
        private final Board board;
        private final Node prev;
        private final int moves;
        private final int priority;

        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            priority = moves + board.manhattan();
        }

        public int compareTo(Node that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return (solutionNodes.get(solutionNodes.size() - 1).board.isGoal());
    }

    // min number of moves to solve initial board
    public int moves() {
        if (!isSolvable()) return -1;
        return solutionNodes.get(solutionNodes.size() - 1).moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> stackofBoards = new Stack<Board>();
        Node newNode = solutionNodes.get(solutionNodes.size() - 1);
        while (newNode.moves != 0) {
            stackofBoards.push(newNode.board);
            newNode = newNode.prev;
        }
        stackofBoards.push(newNode.board);
        return stackofBoards;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
