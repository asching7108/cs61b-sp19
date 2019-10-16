package bearmaps.test;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertNotNull;

/** Test of the autocomplete and search part of the assignment.
 *
 * @author Hsingyi Lin
 * date    10/14/2019
 */

public class TestAutocompleteAndSearch {

    private static final String OSM_DB_PATH = "data/proj2c_xml/berkeley-2019.osm.xml";
    private static AugmentedStreetMapGraph graph;
    private static boolean initialized = false;

    @Before
    public void setUp() throws Exception {
        if (initialized) {
            return;
        }
        graph = new AugmentedStreetMapGraph(OSM_DB_PATH);
        initialized = true;
    }

    @Test
    public void testAutocomplete() {
        ArrayList<String> res = (ArrayList) graph.getLocationsByPrefix("123mon456");
        for (String s : res) {
            System.out.println(s);
        }
        assertNotNull(res);
    }

    @Test
    public void testSearch() {
        List<Map<String, Object>> res = graph.getLocations("Bongo Burger");
        for (Map m : res) {
            for (Object o : m.keySet()) {
                System.out.println(o + " = " + m.get(o));
            }
            System.out.println();
        }
        assertNotNull(res);
    }

}