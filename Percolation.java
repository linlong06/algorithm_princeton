/* *****************************************************************************
 *  Name: Long Lin
 *  Date: 2019-10-04
 *  Description: percolation.java
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private final int size;
    private final int top;
    private final int bot;
    private int numOpensites;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufNobot;

    // grid is the variable of 2D array with size of n; 0 means blocked, and 1 means open
    // create site variable to convert row/col index to integer for Union Find calculation


    // creates n-by-n grid, with all sites initially blocked
    // grid is the variable of 2D array with n-size;
    // grid = 0 means blocked' and grid = 1 means open
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid Input!");
        }
        grid = new boolean[n][n];
        size = n;
        top = n * n;
        bot = n * n + 1;
        numOpensites = 0;
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufNobot = new WeightedQuickUnionUF(n * n + 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }

    }

    // opens the site (row, col) if it is not open already
    // need to raise error if row/col is out of bound??
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            numOpensites++;
        }
        // union the opened site with top/bot virtue site if this site is on top/bot row
        if (row == 1) {
            uf.union(sites(row, col), top);
            ufNobot.union(sites(row, col), top);
        }
        if (row == size) {
            uf.union(sites(row, col), bot);
        }
        // union the opened site with its neiboring open site
        if (row >= 2 && grid[row - 2][col - 1]) {
            uf.union(sites(row, col), sites(row - 1, col));
            ufNobot.union(sites(row, col), sites(row - 1, col));
        }
        if (row <= size - 1 && grid[row][col - 1]) {
            uf.union(sites(row, col), sites(row + 1, col));
            ufNobot.union(sites(row, col), sites(row + 1, col));
        }
        if (col >= 2 && grid[row - 1][col - 2]) {
            uf.union(sites(row, col), sites(row, col - 1));
            ufNobot.union(sites(row, col), sites(row, col - 1));
        }
        if (col <= size - 1 && grid[row - 1][col]) {
            uf.union(sites(row, col), sites(row, col + 1));
            ufNobot.union(sites(row, col), sites(row, col + 1));
        }

    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return (grid[row - 1][col - 1]);
    }

    // is the site (row, col) full?
    // step 1: check if this site is open
    // step 2: check if this site is connected to any of the top
    public boolean isFull(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        return (ufNobot.connected(sites(row, col), top));

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpensites;
    }

    // does the system percolate?
    public boolean percolates() {
        return (uf.connected(top, bot));
    }

    // validate if row or col index is out of bound
    private void validate(int row, int col) {
        if (row <= 0 || row > size) {
            throw new IllegalArgumentException("index " + row + " is out of bound");
        }
        if (col <= 0 || col > size) {
            throw new IllegalArgumentException("index " + col + " is out of bound");
        }
    }

    // convert row and col index into integer index
    private int sites(int row, int col) {
        validate(row, col);
        int sites = size * (row - 1) + col - 1;
        return sites;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation pc = new Percolation(n);
        while (!pc.percolates()) {
            int row = StdRandom.uniform(n) + 1;
            int col = StdRandom.uniform(n) + 1;
            pc.open(row, col);
        }
        double fraction = (double) pc.numberOfOpenSites() / (n * n);
        StdOut.println("Percolation threshold = " + String.format("%.2f", fraction));
    }
}
