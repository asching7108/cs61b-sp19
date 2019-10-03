package bearmaps;

import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.*;

/**
 * @author Hsingyi Lin
 * date    10/03/2019
 */

public class ArrayHeapMinPQTest {

    @Test
    public void TestContains() {
        ArrayHeapMinPQ<Integer> t = new ArrayHeapMinPQ<>();
        t.add(10, 1);
        t.add(20, 3);
        t.add(30, 2);
        assertFalse(t.contains(40));
        assertTrue(t.contains(20));
    }

    @Test
    public void TestAdd() {
        ArrayHeapMinPQ<Integer> t = new ArrayHeapMinPQ<>();
        t.add(10, 1);
        t.add(20, 3);
        t.add(30, 2);
        t.add(100, 0.5);
        t.add(25, 4);
        t.add(55, 1.5);
        assertEquals(100, (int) t.getSmallest());
    }

    @Test
    public void TestRemoveSmallest() {
        ArrayHeapMinPQ<Integer> t = new ArrayHeapMinPQ<>();
        t.add(10, 1);
        t.add(20, 3);
        t.add(30, 2);
        t.add(100, 0.5);
        t.add(25, 4);
        t.add(55, 1.5);
        assertEquals(100, (int) t.removeSmallest());
        assertEquals(10, (int) t.getSmallest());
    }

    @Test
    public void TestChangePriority() {
        ArrayHeapMinPQ<Integer> t = new ArrayHeapMinPQ<>();
        t.add(10, 1);
        t.add(20, 3);
        t.add(30, 2);
        t.add(100, 0.5);
        t.add(25, 4);
        t.add(55, 1.5);
        t.changePriority(10, 10);
        t.changePriority(20, 0.2);
        assertEquals(20, (int) t.getSmallest());
    }

    @Test
    public void TimingTest() {
        Random r = new Random();
        NaiveMinPQ<Integer> n = new NaiveMinPQ<>();
        ArrayHeapMinPQ<Integer> a = new ArrayHeapMinPQ<>();
        for (int i = 0; i < 1000000; i++) {
            n.add(i, r.nextDouble());
            a.add(i, r.nextDouble());
        }
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            //n.removeSmallest();
            n.changePriority(r.nextInt(1000000), r.nextDouble());
        }
        long end1 = System.currentTimeMillis();
        System.out.println("Operation time of NaivePQ: " + (end1 - start1)/1000.0 +  " seconds.");

        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            //a.removeSmallest();
            a.changePriority(r.nextInt(1000000), r.nextDouble());
        }
        long end2 = System.currentTimeMillis();
        System.out.println("Operation time of ArrayHeapMinPQ: " + (end2 - start2)/1000.0 +  " seconds.");
    }

}
