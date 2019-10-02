import java.util.*;

/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are < end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 * @author Hsingyi Lin
 * date    10/01/2019
 */

public class FlightSolver {

    private ArrayList<Flight> flights;
    private PriorityQueue<Flight> endTimePQ;
    int maxCount;

    public FlightSolver(ArrayList<Flight> flights) {
        // sort the flights by their start time
        flights.sort(startTimeComparator);
        this.flights = flights;
        // initialize the min-heap priority queue of flights based on the end time
        endTimePQ = new PriorityQueue<>(endTimeComparator);
        maxCount = 0;
        int count = 0;  // keep track of the current passengers in flight
        // loop over each flight
        for (Flight f : flights) {
            // if the end time of the flights in the heap is less than the start
            // time of the flight in loop, remove them from the heap and subtract
            // their passengers from count
            while (!endTimePQ.isEmpty() && endTimePQ.peek().endTime < f.startTime) {
                count-= endTimePQ.peek().passengers;
                endTimePQ.poll();
            }
            // add the flight in the loop to the heap and its passengers to count
            endTimePQ.add(f);
            count += f.passengers;
            // update maxCount when necessary
            if (count > maxCount) {
                maxCount = count;
            }
        }
    }

    public int solve() {
        return maxCount;
    }

    Comparator<Flight> startTimeComparator = (Flight f1, Flight f2) -> {
        if (f1.startTime == f2.startTime) {
            return f1.endTime - f2.endTime;
        }
        return f1.startTime - f2.startTime;
    };

    Comparator<Flight> endTimeComparator = (Flight f1, Flight f2) -> {
        if (f1.endTime == f2.endTime) {
            return f1.startTime - f2.startTime;
        }
        return f1.endTime - f2.endTime;
    };

}
