package bearmaps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author Hsingyi Lin
 * date    10/05/2019
 */

public class KDTreeTest {
    @Test
    public void BasicTest() {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        KDTree kdt = new KDTree(List.of(p1, p2, p3));
        Point ret = kdt.nearest(3.0, 4.0); // returns p2
        assertTrue(ret.getX() == 3.3 && ret.getY() == 4.4);
    }

    @Test
    public void MoreTest() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 5);
        Point p4 = new Point(3, 3);
        Point p5 = new Point(1, 5);
        Point p6 = new Point(4, 4);
        KDTree kdt = new KDTree(List.of(p1, p2, p3, p4, p5, p6));
        Point ret = kdt.nearest(0, 7); // returns p5
        assertTrue(ret.getX() == 1 && ret.getY() == 5);
    }

    @Test
    public void RandomAndTimingTest() {
        Random r = new Random(1005);
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            points.add(new Point(r.nextDouble(), r.nextDouble()));
        }
        long time1 = 0;
        long time2 = 0;
        NaivePointSet n = new NaivePointSet(points);
        KDTree kdt = new KDTree(points);
        for (int i = 0; i < 10000; i++) {
            double rX = r.nextDouble();
            double rY = r.nextDouble();
            long start1 = System.currentTimeMillis();
            Point pN = n.nearest(rX, rY);
            long end1 = System.currentTimeMillis();
            time1 += end1 - start1;
            long start2 = System.currentTimeMillis();
            Point pKDT = kdt.nearest(rX, rY);
            long end2 = System.currentTimeMillis();
            time2 += end2 - start2;
            assertEquals(pN.getX(), pKDT.getX(), 0.01);
            assertEquals(pN.getY(), pKDT.getY(), 0.01);
        }
        System.out.println("Operation time of NaivePointSet: " + time1/1000.0 +  " seconds.");
        System.out.println("Operation time of KDTreeSet: " + time2/1000.0 +  " seconds.");
    }
}
