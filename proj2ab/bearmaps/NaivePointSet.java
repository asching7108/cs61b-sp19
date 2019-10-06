package bearmaps;

import java.util.List;

/**
 * A naive linear-time solution to solve the problem of finding the closest
 * point to a given coordinate.
 * @author Hsingyi Lin
 * date    10/05/2019
 */

public class NaivePointSet implements PointSet {

    private List<Point> points;

    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    @Override
    public Point nearest(double x, double y) {
        Point in = new Point(x, y);
        double minDist = -1;
        Point nearestP = null;
        for (Point p : points) {
            if (minDist == -1 || Point.distance(in, p) < minDist) {
                minDist = Point.distance(in, p);
                nearestP = p;
            }
        }
        return nearestP;
    }

    public static void main (String arg[]) {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        if (ret.getX() == 3.3 && ret.getY() == 4.4) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
