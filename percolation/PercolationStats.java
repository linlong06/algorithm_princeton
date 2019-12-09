/* *****************************************************************************
 *  Name: Long Lin
 *  Date: 2019-10-04
 *  Description: Percolation Stats
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Invalid Input!");
        }
        double[] fractions = new double[trials];
        double confidence = 1.96;

        for (int i = 0; i < trials; i++) {
            Percolation pc = new Percolation(n);
            while (!pc.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                pc.open(row, col);
            }
            int numSites = pc.numberOfOpenSites();
            fractions[i] = (double) numSites / (n * n);
            mean = StdStats.mean(fractions);
            stddev = StdStats.stddev(fractions);
            confidenceLo = mean - confidence * stddev / Math.sqrt(trials);
            confidenceHi = mean + confidence * stddev / Math.sqrt(trials);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;

    }

    // test client (see below)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        int trials = StdIn.readInt();
        PercolationStats pcs = new PercolationStats(n, trials);
        StdOut.println("mean                    =" + Double.toString(pcs.mean()));
        StdOut.println("stddev                  =" + Double.toString(pcs.stddev()));
        StdOut.println(
                "95% confidence interval = [" + Double.toString(pcs.confidenceLo()) + ", " + Double
                        .toString(pcs.confidenceHi()) + "]");
    }
}
