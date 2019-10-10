package bearmaps.hw4;

import bearmaps.proj2ab.ArrayHeapMinPQ;

import java.util.*;

/**
 * {@code AStarSolver} implements A* algorithm to solve shortest path problems.
 *
 * @author Hsing Yi Lin
 * date    10/10/2019
 */

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private ArrayHeapMinPQ<Vertex> fringes;  // the PQ stores vertices to be visited
    private HashMap<Vertex, Double> distTo;  // distance from the start vertex
    private HashMap<Vertex, Vertex> edgeTo;  // edge leading to the vertices
    private int numStatesExplored;           // number of vertices visited
    private SolverOutcome outcome;           // problem solve outcome
    private List<Vertex> solution;           // vertices of the shortest path in order
    private double solutionWeight;           // total weight of the shortest path
    private double timeSpent;                // total time spent finding the shortest path

    /**
     * Finds the shortest path from {@code start} to {@code end} of the graph {@code input}
     * using a* algorithm. Initialize variables and stores the outcome and solution. If the
     * given timeout {@code timeout} is exceeded, it stops running.
     *
     * The A* algorithm:
     *     Adds the start vertex in the PQ
     *     If PQ is not empty:
     *         remove the smallest vertex p from PQ
     *         if p equals goal:
     *             the shortest path is found
     *         else:
     *         relax all edges outgoing from p
     *
     * @param input the graph
     * @param start the start vertex
     * @param end the goal vertex
     * @param timeout the timeout value
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {

        // Initialize all variables
        fringes = new ArrayHeapMinPQ<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        numStatesExplored = 0;
        solution = new LinkedList<>();
        solutionWeight = 0;
        timeSpent = 0;

        // Adds the start vertex in the PQ
        fringes.add(start, input.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.0);

        // Repeats until the PQ is empty, PQ.getSmallest() is the goal, or timeout is exceeded
        long startTime = System.currentTimeMillis();  // timing start
        while (fringes.size() != 0) {
            Vertex p = fringes.removeSmallest();
            timeSpent = (System.currentTimeMillis() - startTime)/1000.0;
            numStatesExplored++;
            // If goal is found, stops running and updates the solution
            if (p.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                updateSolution(start, end);
                solutionWeight = distTo.get(end);
                return;
            }
            // If timeout is exceeded, stops running
            if (timeSpent > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                return;
            }
            relax(input, end, p);
        }
        // Goal is not found
        timeSpent = (System.currentTimeMillis() - startTime)/1000.0;
        outcome = SolverOutcome.UNSOLVABLE;
    }

    /**
     * For all neighbor vertices of {@code p}, adds or updates the priority of the neighbor
     * vertex q to the {@code fringes} if the distance from start to p + the weight of edge
     * p-q is less than the previous distance from start to q or if q is never considered to
     * be relaxed.
     *
     * @param input the graph
     * @param end the goal vertex
     * @param p the current vertex of which the edges out to be relaxed
     */
    private void relax(AStarGraph<Vertex> input, Vertex end, Vertex p) {
        List<WeightedEdge<Vertex>> neighbors = input.neighbors(p);
        for (WeightedEdge<Vertex> e : neighbors) {
            Vertex q = e.to();
            double w = e.weight();
            if (!distTo.containsKey(q) || distTo.get(p) + w < distTo.get(q)) {
                distTo.put(q, distTo.get(p) + w);
                edgeTo.put(q, p);
                if (fringes.contains(q)) {
                    fringes.changePriority(q, distTo.get(q) + input.estimatedDistanceToGoal(q, end));
                }
                else {
                    fringes.add(q, distTo.get(q) + input.estimatedDistanceToGoal(q, end));
                }
            }
        }
    }

    /**
     * Stores the vertices of the solution to shortest path in order the the list of vertices
     * {@code solution}.
     *
     * @param start the start vertex
     * @param end the goal vertex
     */
    private void updateSolution(Vertex start, Vertex end) {
        solution.add(end);
        Vertex curr = end;
        while (!curr.equals(start)) {
            curr = edgeTo.get(curr);
            solution.add(0, curr);
        }
    }

    @Override
    public SolverOutcome outcome() { return outcome; }

    @Override
    public List<Vertex> solution() { return solution; }

    @Override
    public double solutionWeight() { return solutionWeight; }

    @Override
    public int numStatesExplored() { return numStatesExplored; }

    @Override
    public double explorationTime() { return timeSpent; }
}
