package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**                       Percolation
 * This class represents a percolation system. A system is an N-by-N
 * grid of sites. Each site is either open or blocked. A full site
 * is an open site that can be connected to an open site in the top
 * row via a chain of neighboring open sites. The system percolates
 * if there is a full site in the bottom row.
 * @author Hsingyi Lin
 * date    09/28/2019
 */

public class Percolation {

    private int[][] grid;
    /* Representation of connections of open sites including the
     * virtual top and bottom sites. */
    private WeightedQuickUnionUF sites;
    /* Representation of connections of open sites including only
     * the virtual top site to prevent backwash. */
    private WeightedQuickUnionUF sitesNoBackWash;
    private int virtualTopSite;
    private int virtualBottomSite;
    private int numOpenSite;

    /**
     * Create N-by-N grid, with all sites initially blocked.
     */
    public Percolation(int N) {
        if (N < 1) {
            throw new java.lang.IllegalArgumentException();
        }
        grid = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = 0;
            }
        }
        sites = new WeightedQuickUnionUF(N * N + 2);
        sitesNoBackWash = new WeightedQuickUnionUF(N * N + 1);
        virtualTopSite = N * N;
        virtualBottomSite = N * N + 1;
        numOpenSite = 0;
    }

    /**
     * Return true / false if the system percolates.
     */
    public boolean percolates() {
        return sites.connected(virtualTopSite, virtualBottomSite);
    }

    /**
     * Return the 1D index of the given indices.
     */
    public int xyTo1D(int row, int col) {
        validate(row, col);
        return row * grid[0].length + col;
    }

    /**
     * Return true / false if the site of the given indices is open.
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row][col] == 1;
    }

    /**
     * Return true / false if the site of the given indices is full.
     */
    public boolean isFull(int row, int col) {
        validate(row,col);
        if (sitesNoBackWash.connected(virtualTopSite, xyTo1D(row, col))) {
            return true;
        }
        return false;
    }

    /**
     * Open the site (row, col) if it is not open already.
     */
    public void open(int row, int col) {
        validate(row, col);
        grid[row][col] = 1;
        numOpenSite++;
        // connect both site unions to the virtual top site
        if (row == 0) {
            sites.union(xyTo1D(row, col), virtualTopSite);
            sitesNoBackWash.union(xyTo1D(row, col), virtualTopSite);
        }
        // connect only the regular site union to the virtual bottom site
        if (row == grid.length - 1) {
            sites.union(xyTo1D(row, col), virtualBottomSite);
        }
        // connect both site unions to the side at top
        if (row > 0 && isOpen(row - 1, col)) {
            sites.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            sitesNoBackWash.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        }
        // connect both site unions to the side at bottom
        if (row < grid.length - 1 && isOpen(row + 1, col)) {
            sites.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            sitesNoBackWash.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }
        // connect both site unions to the side at left
        if (col > 0 && isOpen(row, col - 1)) {
            sites.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            sitesNoBackWash.union(xyTo1D(row, col), xyTo1D(row, col - 1));
        }
        // connect both site unions to the side at right
        if (col < grid.length - 1 && isOpen(row, col + 1)) {
            sites.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            sitesNoBackWash.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }
    }

    /**
     * Return the number of open sites.
     */
    public int numberOfOpenSites() {
        return numOpenSite;
    }

    /**
     * Validate that the given index is between 0 and grid length - 1.
     */
    private void validate(int row, int col) {
        if (row < 0 || row >= grid.length) {
            throw new java.lang.IndexOutOfBoundsException("row index out of bound");
        }
        if (col < 0 || col >= grid.length) {
            throw new java.lang.IndexOutOfBoundsException("column index out of bound");
        }
    }

}
