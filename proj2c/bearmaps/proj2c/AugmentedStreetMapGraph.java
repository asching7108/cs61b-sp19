package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.lab9.LocationTrieSet;
import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private HashMap<Point, Long> points;
    private KDTree kdtree;
    private LocationTrieSet trie;

    /**
     * Initializes a map consist of the nodes of the input graph file and
     * their corresponding points (only includes those have neighbors).
     *
     * @param dbPath
     *
     * Filled out by Hsingyi Lin 10/14/2019.
     */
    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        List<Node> nodes = this.getNodes();

        /* Initializes a map of points and node ids consisting of only those have neighbors. */
        points = new HashMap<>();
        for (Node n : nodes) {
            if (!neighbors(n.id()).isEmpty()) {
                points.put(new Point(n.lon(), n.lat()), n.id());
            }
        }

        /* Initializes a kd tree for closest method. */
        kdtree = new KDTree(new ArrayList(points.keySet()));

        /* Initializes a trie for the autocomplete and search method. */
        trie = new LocationTrieSet();
        for (Node n : nodes) {
            if (n.name() != null && n.name().length() > 0) {
                trie.add(n.name(), n);
            }
        }
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     *
     * Filled out by Hsingyi Lin 10/14/2019.
     */
    public long closest(double lon, double lat) {
        Point closestP = kdtree.nearest(lon, lat);
        return points.get(closestP);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     *
     * @param prefix Prefix string to be searched for. Could be any case, with or without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     *
     * Filled out by Hsingyi Lin 10/14/2019.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return trie.keysWithPrefix(cleanString(prefix));
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     *
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     *
     * Filled out by Hsingyi Lin 10/14/2019.
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        LinkedList<Map<String, Object>> res = new LinkedList<>();
        List<Node> match = trie.idsMatched(locationName);
        if (match == null) {
            return null;
        }
        for (Node n : match) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", n.id());
            map.put("name", n.name());
            map.put("lon", n.lon());
            map.put("lat", n.lat());
            res.add(map);
        }
        return res;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
