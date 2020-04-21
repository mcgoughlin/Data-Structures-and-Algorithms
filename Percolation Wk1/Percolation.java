// * *****************************************************************************
// *  Name: Billy McGough
//  *  Date:27/03/2020
//  *  Description: Is anyone going to read this?
//  **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // 0 is closed, 1 is open
    private int openCount;
    private final int nn;
    private final boolean[][] status;
    private final WeightedQuickUnionUF qU;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1 || Math.abs(n) > 2147483645) {
            throw new IllegalArgumentException("Arguments must be positive integers");
        }
        nn = n;
        openCount = 0;
        int endNum = (n * n) + 2;

        qU = new WeightedQuickUnionUF(endNum);
        n++;
        status = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                status[i][j] = false;
            }
        }
        n--;
        for (int j = 1; j <= n; j++) {
            qU.union(j, 0);
        }
        for (int j = endNum - (n + 1); j < endNum - 1; j++) {
            qU.union(j, endNum - 1);
        }

    }

    private int helperIndexer(int row, int col) {
        return (nn * (row - 1) + col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || Math.abs(col) > 2147483645 || Math.abs(row) > 2147483645) {
            throw new IllegalArgumentException("Arguments must be positive integers");
        }
        return status[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || Math.abs(col) > 2147483645 || Math.abs(row) > 2147483645) {
            throw new IllegalArgumentException("Arguments must be positive integers");
        }
        int i = nn * (row - 1) + col;
        return qU.find(i) == qU.find(0) && isOpen(row, col);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || col < 1 || Math.abs(col) > 2147483645 || Math.abs(row) > 2147483645) {
            throw new IllegalArgumentException("Arguments must be positive integers");
        }
        if (!isOpen(row, col)) {
            status[row - 1][col - 1] = true;
            if (row > 1) {
                if (isOpen(row - 1, col)) {
                    qU.union(helperIndexer(row, col), helperIndexer(row - 1, col));
                }
            }
            if (row < nn) {
                if (isOpen(row + 1, col)) {
                    qU.union(helperIndexer(row, col), helperIndexer(row + 1, col));
                }
            }
            if (col > 1) {
                if (isOpen(row, col - 1)) {
                    qU.union(helperIndexer(row, col), helperIndexer(row, col - 1));
                }
            }
            if (col < nn) {
                if (isOpen(row, col + 1)) {
                    qU.union(helperIndexer(row, col), helperIndexer(row, col + 1));
                }
            }
            openCount++;
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        int i = (nn * nn) + 1, j = 0;
        return qU.find(i) == qU.find(j);
    }
}
