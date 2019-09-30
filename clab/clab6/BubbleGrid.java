import java.util.ArrayList;
import java.util.LinkedList;

/**                    Falling Bubbles
 * Sticky bubbles are hanging from the ceiling. This class has
 * a grid of bubbles, and provides methods for the users to figure
 * out how many bubbles will drop as they pop them.
 * @Author Hsingyi Lin
 * date    09/26/2019
 */

public class BubbleGrid {
    private int[][] grid;
    private int numBubbles;
    private LinkedList<Position> bubblePos;
    private UnionFind bubbleSet;

    /**
     * Helper class that holds the x and y coordinates to indicate
     * a position.
     */
    private class Position {
        int row;
        int col;

        public Position(int x, int y) {
            row = x;
            col = y;
        }

        public int row() { return row; }

        public int col() { return col; }

    }

    /**
     *  Create new BubbleGrid with bubble/space locations specified by grid.
     * Grid is composed of only 1's and 0's, where 1's denote a bubble, and
     * 0's denote a space.
     */
    public BubbleGrid(int[][] grid) {
        this.grid = grid;
        this.grid = grid;
        bubblePos = new LinkedList<>();

        /* Compute numBubbles and update bubbles array with the
         * position of each bubbles. */
        numBubbles = 0;
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    bubblePos.add(new Position(i, j));
                    numBubbles++;
                }
            }
        }
        fillBubbleSet();
    }

    /**
     * Returns the index of the given position in the bubblePos array.
     */
    private int indexOf(int x, int y) {
        for (int i = 0; i < numBubbles; i++) {
            if (bubblePos.get(i).row() == x && bubblePos.get(i).col() == y) {
                return i;
            }
        }
        return -1;
    }

    /**
     *  Returns an array whose i-th element is the number of bubbles that
     * fall after the i-th dart is thrown. Assume all elements of darts
     * are unique, valid locations in the grid. Must be non-destructive
     * and have no side-effects to grid.
     */
    public int[] popBubbles(int[][] darts) {
        int[] unstuckBubbles = new int[darts.length];
        for (int i = 0; i < darts.length; i++) {
            unstuckBubbles[i] = updateBubbleSet(darts[i][0], darts[i][1]);
        }
        return unstuckBubbles;
    }

        /**
         * Creates and update the bubbleSet. If a bubble is at the bottom
         * or right of another bubble, union them.
         * */
    private void fillBubbleSet() {
        bubbleSet = new UnionFind(numBubbles);
        for (int i = 0; i < numBubbles; i++) {
            if (bubblePos.get(i).row() > 0
                    && grid[bubblePos.get(i).row() - 1][bubblePos.get(i).col()] == 1) {
                bubbleSet.union(i,
                        indexOf(bubblePos.get(i).row() - 1, bubblePos.get(i).col()));
            }
            if (bubblePos.get(i).col() > 0
                    && grid[bubblePos.get(i).row()][bubblePos.get(i).col() - 1] == 1) {
                bubbleSet.union(i, i - 1);
            }
        }
        // System.out.println(bubbleSet);
    }

    /**
     * Updates the bubbleSet by removing the bubble with the given position
     * and redo union the left bubbles, and returns the number of bubbles
     * that are not connected to any bubble in the topmost row.
     */
    private int updateBubbleSet(int x, int y) {
        if (grid[x][y] == 0) {
            return 0;
        }

        /* Remove popped bubble and update bubbleSet. */
        numBubbles--;
        grid[x][y] = 0;
        bubblePos.remove(indexOf(x, y));
        fillBubbleSet();

        /* Compute the number of unstuck bubbles and remove them. */
        int numUnstuckBubble = 0;
        for (int i = numBubbles - 1; i >= 0; i--) {
            if (bubblePos.get(bubbleSet.find(i)).row() > 0) {
                numUnstuckBubble++;
                grid[bubblePos.get(i).row()][bubblePos.get(i).col()] = 0;
                bubblePos.remove(i);
            }
        }

        /* Update bubbleSet. */
        numBubbles -= numUnstuckBubble;
        fillBubbleSet();
        return numUnstuckBubble;
    }
}
