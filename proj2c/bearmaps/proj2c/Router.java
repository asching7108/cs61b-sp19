package bearmaps.proj2c;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import bearmaps.hw4.*;

/**
 * This class acts as a helper for the RoutingAPIHandler.
 * @author Josh Hug, ______
 */
public class Router {

    /**
     * Overloaded method for shortestPath that has flexibility to specify a solver
     * and returns a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(AugmentedStreetMapGraph g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long src = g.closest(stlon, stlat);
        long dest = g.closest(destlon, destlat);
        return new AStarSolver<>(g, src, dest, 20).solution();
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     *
     * Filled out by Hsingyi Lin 10/15/2019.
     */
    public static List<NavigationDirection> routeDirections(AugmentedStreetMapGraph g, List<Long> route) {
        /* If there's only 0 or 1 node on the route, return null since no route
           can be formed. */
        if (route.size() < 2) {
            return null;
        }
        LinkedList<NavigationDirection> res = new LinkedList<>();
        /* Initializes the first nav. direction. */
        Long last = route.remove(0);
        route.add(last);              // always add the node back to avoid changing the route
        Long curr = route.remove(0);
        route.add(curr);
        NavigationDirection init = createNav(g, last, curr, 0);
        res.add(init);
        /* If there's only 2 node on the route, return the nav. direction. */
        if (route.size() < 3) {
            return res;
        }
        /* For all the rest nodes, considers the previous and current bearings and ways. */
        for (int i = 0; i < route.size() - 2; i++) {
            Long next = route.remove(0);
            route.add(next);
            String currWay = res.getLast().way;
            String nextWay = g.edgeName(next, curr);
            double currBearing = NavigationDirection.bearing(g.lon(last), g.lon(curr), g.lat(last), g.lat(curr));
            double nextBearing = NavigationDirection.bearing(g.lon(curr), g.lon(next), g.lat(curr), g.lat(next));
            int direction = NavigationDirection.getDirection(currBearing, nextBearing);
            /* If both ways are null or have identical names, adds the new distance to the
               previous nav. direction. */
            if ((nextWay == null && Objects.equals(currWay, NavigationDirection.UNKNOWN_ROAD))
                    || (nextWay != null && nextWay.equals(currWay))) {
                res.getLast().distance += g.distance(curr, next);
            }
            /* Otherwise both ways have different names, creates new nav. direction. */
            else {
                NavigationDirection nav = createNav(g, curr, next, direction);
                res.add(nav);
            }
            last = curr;
            curr = next;
        }
        return res;
    }

    /**
     * Helper function to create and return a nav. direction by the given inputs.
     *
     * @param g The graph to use.
     * @param from The id of the first node
     * @param to the id of the second node
     * @param drct the direction from the first node to the second
     * @return the nav. direction created
     *
     * Filled out by Hsingyi Lin 10/15/2019.
     */
    private static NavigationDirection createNav(AugmentedStreetMapGraph g,
                                          Long from,
                                          Long to,
                                          int drct) {
        NavigationDirection nav = new NavigationDirection();
        nav.direction = drct;
        nav.distance = g.distance(from, to);
        if (g.edgeName(from, to) != null) {
            nav.way = g.edgeName(from, to);
        }
        return nav;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for. This is only
     * useful for Part IV of the project.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        /** Checks that a value is between the given ranges.*/
        private static boolean numInRange(double value, double from, double to) {
            return value >= from && value <= to;
        }

        /**
         * Calculates what direction we are going based on the two bearings, which
         * are the angles from true north. We compare the angles to see whether
         * we are making a left turn or right turn. Then we can just use the absolute value of the
         * difference to give us the degree of turn (straight, sharp, left, or right).
         * @param prevBearing A double in [0, 360.0]
         * @param currBearing A double in [0, 360.0]
         * @return the Navigation Direction type
         */
        private static int getDirection(double prevBearing, double currBearing) {
            double absDiff = Math.abs(currBearing - prevBearing);
            if (numInRange(absDiff, 0.0, 15.0)) {
                return NavigationDirection.STRAIGHT;

            }
            if ((currBearing > prevBearing && absDiff < 180.0)
                    || (currBearing < prevBearing && absDiff > 180.0)) {
                // we're going right
                if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
                    // bearmaps.proj2c.example of high abs diff is prev = 355 and curr = 2
                    return NavigationDirection.SLIGHT_RIGHT;
                } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
                    return NavigationDirection.RIGHT;
                } else {
                    return NavigationDirection.SHARP_RIGHT;
                }
            } else {
                // we're going left
                if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
                    return NavigationDirection.SLIGHT_LEFT;
                } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
                    return NavigationDirection.LEFT;
                } else {
                    return NavigationDirection.SHARP_LEFT;
                }
            }
        }


        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }

        /**
         * Returns the initial bearing (angle) between vertices v and w in degrees.
         * The initial bearing is the angle that, if followed in a straight line
         * along a great-circle arc from the starting point, would take you to the
         * end point.
         * Assumes the lon/lat methods are implemented properly.
         * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
         * @param lonV  The longitude of the first vertex.
         * @param latV  The latitude of the first vertex.
         * @param lonW  The longitude of the second vertex.
         * @param latW  The latitude of the second vertex.
         * @return The initial bearing between the vertices.
         */
        public static double bearing(double lonV, double lonW, double latV, double latW) {
            double phi1 = Math.toRadians(latV);
            double phi2 = Math.toRadians(latW);
            double lambda1 = Math.toRadians(lonV);
            double lambda2 = Math.toRadians(lonW);

            double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
            double x = Math.cos(phi1) * Math.sin(phi2);
            x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
            return Math.toDegrees(Math.atan2(y, x));
        }
    }
}
