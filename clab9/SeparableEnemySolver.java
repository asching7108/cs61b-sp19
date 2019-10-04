import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Method isSeparable() related methods filled by Hsingyi Lin 10/04/2019.
 * source: https://github.com/junhaowww/cs61b-sp18/blob/master/clab9/SeparableEnemySolver.java
 * Interpreted and rewritten without copying the source code.
 */

public class SeparableEnemySolver {

    Graph g;

    /**
     * Creates a SeparableEnemySolver for a file with name filename. Enemy
     * relationships are biderectional (if A is an enemy of B, B is an enemy of A).
     */
    SeparableEnemySolver(String filename) throws java.io.FileNotFoundException {
        this.g = graphFromFile(filename);
    }

    /** Alterntive constructor that requires a Graph object. */
    SeparableEnemySolver(Graph g) {
        this.g = g;
    }

    /**
     * Returns true if input is separable into two groups, false otherwise.
     * Pseudocode:
     * --
     * Initialize an empty map M for vertices and their associated group
     * For each vertices v:
     *     If v is not in M (not visited)
     *         If dfs(v, A) returns false (A is an arbitrary group)
     *             Return false (not separable)
     * Return true (separable)
     * --
     * For all vertices v (that have not been visited, i.e. are not connected to
     * any previously vertices, thus can be considered independently) , Recursively
     * goes depth-first through all vertices connected to v, putting direct enemies
     * into two separate groups, then detect if there's already enemies in the group
     * in which we're putting the current vertex, if so then the answer is not separable.
     * If we successfully go through all vertices, the answer is separable.
     *
     */
    public boolean isSeparable() {
        HashMap<String, String> groups = new HashMap<>();  // key: vertices, value: group
        for (String v : g.labels()) {
            if (!groups.containsKey(v)) {
                String groupName = "A";
                boolean res = dfs(v, groupName, groups);
                if (res == false) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if input is separable into three groups, false otherwise.
     * Pseudocode:
     * --
     * Initialize an empty map M for vertices and their associated group
     * For each vertices v:
     *     If v is not in M (not visited)
     *         If bfs(v, A) returns false (A is an arbitrary group)
     *             Return false (not separable)
     * Return true (separable)
     * --
     * Goes breadth-first through all vertices connected to v.
     */
    public boolean isSeparableThree() {
        HashMap<String, String> groups = new HashMap<>();  // key: vertices, value: group
        for (String v : g.labels()) {
            if (!groups.containsKey(v)) {
                String groupName = "A";
                boolean res = bfsThree(v, groupName, groups);
                if (res == false) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Pseudocode:
     * --
     * Add (v, A) to M
     * For each neighbor n of v
     *     If n is not in M (not visited)
     *         If dfs(n, B) returns false (B is the other arbitrary group)
     *             Return false (not separable)
     *     Else (visited)
     *         If M.get(v) equals M.get(n) (v and its neighbor n are in the same group)
     *             Return false (not separable)
     * Return true (separable)
     *--
     */
    private boolean dfs(String v, String group, HashMap<String, String> groups) {
        groups.put(v, group);
        for (String n : g.neighbors(v)) {
            if (!groups.containsKey(n)) {
                boolean res = dfs(n, group.equals("A") ? "B" : "A", groups);
                if (res == false) {
                    return false;
                }
            }
            else {
                if (groups.get(n).equals(group)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Pseudocode:
     * --
     * Initiate an empty List Q of vertices
     * Add (v, A) to M
     * Add v to Q's end
     * While Q is not empty
     *     Let x = dequeue Q
     *     For each neighbor n of x
     *         If n is not in M (not visited)
     *             If M.get(x) = B, let n's group = A, otherwise n's group = B
     *             Add (n, n's group) to M
     *             Add n to Q's end
     *         Else (visited)
     *             If M.get(x) = B and M.get(n) = B
     *                 M.set(n, C)
     *             If M.get(x) = C and M.get(n) = C
     *                 M.set(x, A)
     *                 Add x to Q's front (in case some n has been looped before x is changed)
     *             If M.get(x) = A and M.get(n) = A
     *                 Return false
     * Return true
     * --
     * Seems to work for the given tests, but haven't been tested thoroughly.
     */
    private boolean bfsThree(String v, String group, HashMap<String, String> groups) {
        LinkedList<String> queue = new LinkedList<>();
        groups.put(v, group);
        queue.addLast(v);
        while (!queue.isEmpty()) {
            String x = queue.removeFirst();
            for (String n : g.neighbors(x)) {
                if (!groups.containsKey(n)) {
                    if (groups.get(x) == "C") {
                    }
                    String xGroup = "B";
                    if (groups.get(x) == "B") {
                        xGroup = "A";
                    }
                    groups.put(n, xGroup);
                    queue.addLast(n);
                }
                else {
                    if (groups.get(x) == "B" && groups.get(n) == "B") {
                        groups.put(n, "C");
                    }
                    else if (groups.get(x) == "C" && groups.get(n) == "C") {
                        groups.put(x, "A");
                        queue.addFirst(x);
                    }
                    else if (groups.get(x) == "A" && groups.get(n) == "A") {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /* HELPERS FOR READING IN CSV FILES. */

    /**
     * Creates graph from filename. File should be comma-separated. The first line
     * contains comma-separated names of all people. Subsequent lines each have two
     * comma-separated names of enemy pairs.
     */
    private Graph graphFromFile(String filename) throws FileNotFoundException {
        List<List<String>> lines = readCSV(filename);
        Graph input = new Graph();
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                for (String name : lines.get(i)) {
                    input.addNode(name);
                }
                continue;
            }
            assert(lines.get(i).size() == 2);
            input.connect(lines.get(i).get(0), lines.get(i).get(1));
        }
        return input;
    }

    /**
     * Reads an entire CSV and returns a List of Lists. Each inner
     * List represents a line of the CSV with each comma-seperated
     * value as an entry. Assumes CSV file does not contain commas
     * except as separators.
     * Returns null if invalid filename.
     *
     * @source https://www.baeldung.com/java-csv-file-array
     */
    private List<List<String>> readCSV(String filename) throws java.io.FileNotFoundException {
        List<List<String>> records = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            records.add(getRecordFromLine(scanner.nextLine()));
        }
        return records;
    }

    /**
     * Reads one line of a CSV.
     *
     * @source https://www.baeldung.com/java-csv-file-array
     */
    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        Scanner rowScanner = new Scanner(line);
        rowScanner.useDelimiter(",");
        while (rowScanner.hasNext()) {
            values.add(rowScanner.next().trim());
        }
        return values;
    }

    /* END HELPERS  FOR READING IN CSV FILES. */

}
