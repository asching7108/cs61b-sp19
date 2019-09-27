import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @Author Hsingyi Lin
 * date    09/26/2019
 */

public class TestBubbleGrid {
    @Test
    public void BasicTest() {
        int[][] grid = new int[][] {{1, 1, 0},
                                    {1, 0, 0},
                                    {1, 1, 0},
                                    {1, 1, 1}};
        BubbleGrid bg = new BubbleGrid(grid);
        int [][] darts = new int[][] {{2, 2}, {2, 0}};
        int[] scores = bg.popBubbles(darts);
        int[] expected = new int[] {0, 4};
        assertArrayEquals(expected, scores);
    }

    @Test
    public void TestBiggerGrid() {
        int[][] grid = new int[][] {{1, 1, 0, 0, 1},
                                    {1, 0, 0, 0, 1},
                                    {1, 1, 0, 1, 1},
                                    {1, 1, 1, 0, 1}};
        BubbleGrid bg = new BubbleGrid(grid);
        int [][] darts = new int[][] {{2, 2}, {2, 0}, {1, 4}};
        int[] scores = bg.popBubbles(darts);
        int[] expected = new int[] {0, 4, 3};
        assertArrayEquals(expected, scores);
    }
}
