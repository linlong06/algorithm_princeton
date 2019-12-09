/* *****************************************************************************
 *  Name: Long Lin
 *  Date: 2019-10-17
 *  Description: 8 puzzle problem
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private final int n;
    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String string = Integer.toString(n) + "\n";
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                string = string + tiles[row][col] + "\t";
            }
            string = string + "\n";
        }
        return string;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (!(row == n - 1 && col == n - 1) && tiles[row][col] != n * row + col + 1) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int tile = tiles[row][col];
                if (tile != 0) {
                    int tileRow = (tile - 1) / n;
                    int tileCol = (tile - 1) % n;
                    manhattan = manhattan + Math.abs(tileRow - row) + Math.abs(tileCol - col);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (!(row == n - 1 && col == n - 1) && tiles[row][col] != n * row + col + 1) {
                    return false;
                }
            }
        }
        return (tiles[n - 1][n - 1] == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        for (int row = 0; row < this.n; row++) {
            for (int col = 0; col < this.n; col++) {
                if (this.tiles[row][col] != that.tiles[row][col]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards = new ArrayList<Board>();
        int row = -1;
        int col = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        if (row > 0 && row < n - 1) {
            boards.add(this.exch(row, col, row - 1, col));
            boards.add(this.exch(row, col, row + 1, col));
        }
        else if (row == 0) {
            boards.add(this.exch(row, col, row + 1, col));
        }
        else if (row == n - 1) {
            boards.add(this.exch(row, col, row - 1, col));
        }
        if (col > 0 && col < n - 1) {
            boards.add(this.exch(row, col, row, col - 1));
            boards.add(this.exch(row, col, row, col + 1));
        }
        else if (col == 0) {
            boards.add(this.exch(row, col, row, col + 1));
        }
        else if (col == n - 1) {
            boards.add(this.exch(row, col, row, col - 1));
        }
        return boards;

    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int i = 0;
        if (tiles[i / n][i % n] == 0) i++;
        int row1 = i / n;
        int col1 = i % n;
        i++;
        if (tiles[i / n][i % n] == 0) i++;
        int row2 = i / n;
        int col2 = i % n;
        return this.exch(row1, col1, row2, col2);
    }

    private Board exch(int row1, int col1, int row2, int col2) {
        int[][] newTiles = new int[n][n];
        int a = tiles[row1][col1];
        int b = tiles[row2][col2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == row1 && j == col1) newTiles[i][j] = b;
                else if (i == row2 && j == col2) newTiles[i][j] = a;
                else newTiles[i][j] = tiles[i][j];
            }
        }
        return new Board(newTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // test for equals
        StdOut.println("Board to string: " + initial.toString());
        Board twin1 = initial.twin();
        StdOut.println("Twin1 to string: " + twin1.toString());
        Board twin2 = initial.twin();
        StdOut.println("Twin2 to string: " + twin2.toString());
        StdOut.println("Compare twin1 and twin2: " + twin1.equals(twin2));

        // print out a couple of methods from the initial instance
        /* StdOut.println("Board to string: " + initial.toString());
        StdOut.println("Size of board: " + initial.dimension());
        StdOut.println("Hamming distance: " + initial.hamming());
        StdOut.println("Manhattan distance: " + initial.manhattan());
        StdOut.println("Is it goal? " + initial.isGoal());
        StdOut.println("Twin to string: " + initial.twin().toString());
        StdOut.println("Compare twin and initial: " + initial.equals(initial.twin()));
        StdOut.println("========");
        for (Board board : initial.neighbors()) {
            StdOut.println("Neighboring boards: " + board.toString());
        } */
    }

}
