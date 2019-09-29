package hw2;

import edu.princeton.cs.introcs.*;

/**                      PercolationStats
 * Use Monte Carlo simulation to estimate the percolation threshold.
 * @author Hsingyi Lin
 * date    09/28/2019
 */

public class PercolationStats {

    private Percolation[] P;
    private int[] threshold;
    private int T;

    /**
     * Perform T independent experiments on an N-by-N grid.
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N < 1 || T < 1){
            throw new java.lang.IllegalArgumentException();
        }
        // initialize variables
        this.T = T;
        P = new Percolation[T];
        threshold = new int[T];
        for (int i = 0; i < T; i++) {
            P[i] = pf.make(N);
        }
        // perform T experiments and record thresholds
        for (int i = 0; i < T; i++) {
            threshold[i] = 0;
            for (int j = 1; j <= N * N; j++) {
                int r = StdRandom.uniform(N * N);
                while (P[i].isOpen(r / N, r % N)) {
                    r = StdRandom.uniform(N * N);
                }
                P[i].open(r / N, r % N);
                threshold[i]++;
                if (P[i].percolates()) {
                    break;
                }
            }
        }
        /* test
        for (int i = 0; i < T; i++) {
            System.out.println(threshold[i]);
        } */
    }

    /**
     * Return the sample mean of percolation threshold.
     */
    public double mean() {
        return StdStats.mean(threshold);
    }

    /**
     * Return the sample standard deviation of percolation threshold.
     */
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    /**
     * Return the low endpoint of 95% confidence interval.
     */
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    /**
     * Return the high endpoint of 95% confidence interval.
     */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

}
