// * *****************************************************************************
// *  Name: Billy McGough
//  *  Date:27/03/2020
//  *  Description: Is anyone going to read this?
//  **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double ntr;
    private final int[] results;
    private final int nn;
    private final double confidence95;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("Arguments must be positive integers");
        }
        ntr = trials;
        results = new int[trials];
        nn = n * n;
        confidence95 = 1.96;
        for (int i = 0; i < ntr; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int aRan = StdRandom.uniform(1, n + 1), bRan = StdRandom.uniform(1, n + 1);
                perc.open(aRan, bRan);
            }
            results[i] = perc.numberOfOpenSites();
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results) / nn;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results) / nn;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = StdStats.mean(results) / nn;
        double std = StdStats.stddev(results) / nn;
        return mean - confidence95 * std / Math.sqrt(ntr);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = StdStats.mean(results) / nn;
        double std = StdStats.stddev(results) / nn;
        return mean - confidence95 * std / Math.sqrt(ntr);
    }


    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]), t = Integer.parseInt(args[1]);
        // int n = in.readInt(), t = in2.readInt();
        PercolationStats PS = new PercolationStats(n, t);
        double mean = PS.mean(), std = PS.stddev(), lo = PS.confidenceLo(), hi = PS.confidenceHi();
        StdOut.println("mean: " + mean);
        StdOut.println("stddev: " + std);
        StdOut.println("95% Confidence interval: [" + lo + ", " + hi + "]");


    }

}
