import org.junit.Test;
import static org.junit.Assert.*;

public class BubbleGridTest {

    @Test
    public void testBasic() {

        int[][] grid = {{1, 0, 0, 0},
                        {1, 1, 1, 0}};
        int[][] darts = {{1, 0}};
        int[] expected = {2};

        validate(grid, darts, expected);
    }

    @Test
    public void TestBiggerGrid() {
        int[][] grid = new int[][] {{1, 1, 0, 0, 1},
                                    {1, 0, 0, 0, 1},
                                    {1, 1, 0, 1, 1},
                                    {1, 1, 1, 0, 1}};
        int [][] darts = new int[][] {{2, 2}, {2, 0}, {1, 4}};
        int[] expected = new int[] {0, 4, 3};
        validate(grid, darts, expected);
    }

    private void validate(int[][] grid, int[][] darts, int[] expected) {
        BubbleGrid sol = new BubbleGrid(grid);
        assertArrayEquals(expected, sol.popBubbles(darts));
    }
}
