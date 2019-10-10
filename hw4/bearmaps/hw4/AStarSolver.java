package bearmaps.hw4;

import bearmaps.proj2ab.DoubleMapPQ;

import java.util.*;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private DoubleMapPQ<Vertex> fringes;
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, Vertex> edgeTo;
    private int numStatesExplored;
    private SolverOutcome outcome;
    private List<Vertex> solution;
    private double solutionWeight;
    private double timeSpent;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {

        fringes = new DoubleMapPQ<>();
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
        while (fringes.size() != 0) {
            Vertex p = fringes.removeSmallest();
            numStatesExplored++;
            if (p.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                updateSolution(start, end);
                solutionWeight = distTo.get(end);
                return;
            }
            relax(input, end, input.neighbors(p));
        }
        outcome = SolverOutcome.UNSOLVABLE;
    }

    private void relax(AStarGraph<Vertex> input, Vertex end, List<WeightedEdge<Vertex>> neighbors) {
        for (WeightedEdge<Vertex> e : neighbors) {
            Vertex p = e.from();
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
    public double explorationTime() { return 0; }
}
