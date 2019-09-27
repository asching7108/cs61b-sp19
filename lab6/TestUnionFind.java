import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @Author Hsingyi Lin
 * date    09/26/2019
 */

public class TestUnionFind {
    @Test
    public void testUnionFind() {
        UnionFind uf = new UnionFind(16);
        uf.union(2, 3);
        uf.union(1, 2);
        uf.union(5, 7);
        uf.union(8, 4);
        uf.union(7, 2);
        assertEquals(3, uf.find(2));
        uf.union(0, 6);
        uf.union(0, 4);
        uf.union(0, 2);
        assertEquals(6, uf.parent(0));
        assertEquals(3, uf.find(0));
        assertEquals(9, uf.sizeOf(0));
        System.out.println(uf);
        // Expected parentArray:
        // 6	3	3	-9	3	7	4	3	4	-1	-1	-1	-1	-1	-1	-1
    }

    @Test
    public void testUnionFindWithPathCompression() {
        UnionFind uf = new UnionFind(16);
        uf.unionWithPathCompassion(2, 3);
        uf.unionWithPathCompassion(1, 2);
        uf.unionWithPathCompassion(5, 7);
        uf.unionWithPathCompassion(8, 4);
        uf.unionWithPathCompassion(7, 2);
        assertEquals(3, uf.find(2));
        uf.unionWithPathCompassion(0, 6);
        uf.unionWithPathCompassion(0, 4);
        uf.unionWithPathCompassion(0, 2);
        assertEquals(3, uf.parent(0));
        assertEquals(3, uf.find(0));
        assertEquals(9, uf.sizeOf(0));
        System.out.println(uf);
        // Expected parentArray:
        // 3	3	3	-9	3	7	4	3	4	-1	-1	-1	-1	-1	-1	-1
    }
}
