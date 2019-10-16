package bearmaps.proj2ab;

import java.util.List;

/**
 * A efficient logarithmic-time solution to solve the problem of finding the
 * closest point to a given coordinate using K-D tree data structure.
 * @author Hsingyi Lin
 * date    10/05/2019
 */

public class KDTree implements PointSet{

    private Node root;

    /**
     * Initializes the whole tree with the given list of points.
     *
     * @param points the list of points
     */
    public KDTree(List<Point> points) {
        root = null;
        for (Point p : points) {
            if (root == null) {
                root = new Node(p, 0);      // initialize the root node
            }
            else {
                add(root, new Node(p, 0));  // add node to the tree
            }
        }
    }

    /**
     * Recursively looks through a branch of the tree by comparing the node
     * {@code n} and the node to be added {@code o} to decide going left or
     * right, and add the node to the leaf node.
     *
     * @param n the node that is currently went through
     * @param o the node to be added
     */
    private void add(Node n, Node o) {
        if (n.compareTo(o) > 0) {
            if (n.left == null) {
                n.left = o;
                o.layer = (n.layer + 1) % 2;
            } else {
                add(n.left, o);
            }
        }
        else {
            if (n.right == null) {
                n.right = o;
                o.layer = (n.layer + 1) % 2;
            } else {
                add(n.right, o);
            }
        }
    }

    /**
     * {@code Node} class represents a node on the K-D tree. Each {@code Node}
     * stores a point and its left and right child nodes. The layer indicates:
     * layer = 0: partitions like an X-based Tree
     * layer = 1: partitions like an Y-based Tree
     */
    private static class Node implements Comparable {

        private Point point;
        private int layer;
        private Node left;
        private Node right;

        Node(Point p, int l) {
            point = p;
            layer = l;
            left = null;
            right = null;
        }

        // Compares the two points' x or y value based on the layer.
        @Override
        public int compareTo(Object o) {
            Node other = (Node) o;
            if (layer == 0) {
                return Double.compare(point.getX(), other.point.getX());
            }
            return Double.compare(point.getY(), other.point.getY());
        }

    }

    /**
     * Return the nearest node to the given position.
     *
     * @param x the x-value of the goal point
     * @param y the y-value of the goal point
     * @return the nearest node
     */
    @Override
    public Point nearest(double x, double y) {
        return nearestHelper(root, new Point(x, y), root).point;
    }

    /**
     * Returns whichever is closer to the point {@code goal} out of:
     * 1. the node {@code best}
     * 2. all items in the subtree starting at node {@code n}
     *
     * @param n the node
     * @param goal the point of goal
     * @param best the best (nearest to the goal) node so far
     * @return the nearest node
     */
    private Node nearestHelper(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }
        if (Point.distance(goal, n.point) < Point.distance(goal, best.point)) {
            best = n;
        }
        Node good;
        Node bad;
        if (n.compareTo(new Node(goal, 0)) > 0) {
            good = n.left;
            bad = n.right;
        }
        else {
            good = n.right;
            bad = n.left;
        }
        best = nearestHelper(good, goal, best);
        // If bad side could still has nearer point
        if (badSideUseful(n, goal, best)) {
            best = nearestHelper(bad, goal, best);
        }
        return best;
    }

    /**
     * Returns {@code true} if the bad side of the given node {@code n} could
     * have a nearer point to the point {@code goal} than the node {@code best}.
     *
     * @param n the Node of which the bad side is
     * @param goal the point of goal
     * @param best the best (nearest to the goal) node so far
     * @return {@code true} if bad side could be useful, {@code false} otherwise
     */
    private boolean badSideUseful(Node n, Point goal, Node best) {
        if (n.layer == 0) {
            return Point.distance(new Point(n.point.getX(), goal.getY()), goal)
                    < Point.distance(best.point, goal);
        }
        if (n.layer == 1) {
            return Point.distance(new Point(goal.getX(), n.point.getY()), goal)
                    < Point.distance(best.point, goal);
        }
        return false;
    }

}
