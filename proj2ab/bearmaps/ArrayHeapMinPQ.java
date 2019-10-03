package bearmaps;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * The {@code ArrayHeapMinPQ} class implements the ExtrinsicMinPQ interface,
 * representing a priority queue of generic keys with extrinsic priority.
 * Uses a one-based array to simplify parent and child calculations.
 *
 * @author Hsingyi Lin
 * date    10/03/2019
 */

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private static final int INIT_SIZE = 100;
    private static final double INIT__MIN_LOADFACTOR = 0.25;
    private static final double INIT_MAX_LOADFACTOR = 0.75;

    private PriorityNode[] heap;        // store items at indices 1 to n
    private HashMap<T, Integer> items;  // store items with indices in heap
    private double loadFactorMin;
    private double loadFactorMax;

    /**
     * Initializes an empty priority queue.
     */
    public ArrayHeapMinPQ() {
        heap = new ArrayHeapMinPQ.PriorityNode[INIT_SIZE];
        items = new HashMap<>();
        loadFactorMin = INIT__MIN_LOADFACTOR;
        loadFactorMax = INIT_MAX_LOADFACTOR;
    }

    /**
     * {@code PriorityNode} class stores the item and priority, with comparability
     * among priorities.
     */
    private class PriorityNode implements Comparable<PriorityNode> {

        T item;
        double priority;

        PriorityNode(T i, double p) {
            item = i;
            priority = p;
        }

        T getItem() { return item; }

        double getPriority() { return priority; }

        void setPriority(double p) { priority = p; }

        @Override
        public int compareTo(PriorityNode o) {
            return Double.compare(this.priority, o.priority);
        }
    }

    /**
     * Adds an item of type T with the given priority.
     *
     * @param item the item
     * @param priority the extrinsic priority
     * @throws IllegalArgumentException if {@code item} already exists
     */
    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("argument to add() already exists in PQ");
        }
        if (size() / (double) heap.length > loadFactorMax) {
            resize(heap.length * 2);
        }
        int pos = size() + 1;
        heap[pos] = new PriorityNode(item, priority);
        items.put(item, pos);
        swim(pos);
    }

    /**
     * Recursively swaps the item at {@code x} with its parent item if {@code x} has
     * lower priority than its parent, and so forth.
     *
     * @param x the index of the item at the array
     */
    private void swim(int x) {
        if (heap[x].compareTo(heap[parent(x)]) < 0) {
            swap(x, parent(x));
            swim(parent(x));
        }
    }

    /**
     * Returns the index of the parent item of the item at index {@code x}. If the
     * item at index {@code x} is the root, return its own index.
     *
     * @param x the index of the item at the array
     * @return the index of the parent item
     */
    private int parent(int x) {
        if (x == 1) {
            return 1;
        }
        return x / 2;
    }

    /**
     * Swaps the item at {@code x} with the item at {@code y}.
     *
     * @param x the index of the item at the array
     * @param y the index of the item at the array
     */
    private void swap(int x, int y) {
        if (x == y) {
            return;
        }
        PriorityNode temp = heap[x];
        heap[x] = heap[y];
        heap[y] = temp;
        items.put(heap[x].getItem(), x);
        items.put(heap[y].getItem(), y);
    }

    /**
     * Resize the heap to the given new size.
     *
     * @param newSize the new size
     */
    private void resize(int newSize) {
        PriorityNode[] newHeap = new ArrayHeapMinPQ.PriorityNode[newSize];
        for (int i = 1; i <= size(); i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    /**
     * Returns true if the PQ contains the given {@code item}.
     *
     * @param item the item
     * @return {@code true} if the PQ contains the given {@code item}, {@code false}
     * otherwise.
     */
    @Override
    public boolean contains(T item) {
        if (size() == 0) {
            return false;
        }
        return items.containsKey(item);
    }

    /**
     * Returns the item with smallest priority in the PQ.
     *
     * @return the item with smallest priority
     * @throws NoSuchElementException if PQ is empty
     */
    @Override
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        return heap[1].getItem();
    }

    /**
     * Removes and returns the item with smallest priority in the PQ.
     *
     * @return the item with smallest priority
     * @throws NoSuchElementException if PQ is empty
     */
    @Override
    public T removeSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        if (size() / (double) heap.length < loadFactorMin) {
            resize(heap.length / 2);
        }
        T smallest = heap[1].getItem();
        heap[1] = heap[size()];
        heap[size()] = null;
        if (size() > 0) {
            sink(1);
        }
        items.remove(smallest);
        return smallest;
    }

    /**
     * Recursively swaps the item at {@code x} with its smallest child item if
     * {@code x} has higher priority than its child, and so forth.
     *
     * @param x the index of the item at the array
     */
    private void sink(int x) {
        if (heap[x].compareTo(heap[child(x)]) > 0) {
            int smallestChild = child(x);
            swap(x, smallestChild);
            sink(smallestChild);
        }
    }

    /**
     * Returns the index of the smallest child item of the item at index {@code x}.
     * If the item at index {@code x} has no child, return its own index.
     *
     * @param x the index of the item at the array
     * @return the index of the smallest child item
     */
    private int child(int x) {
        if (size() < x * 2) {
            return x;
        }
        if (size() == x * 2 || heap[x * 2].compareTo(heap[x * 2 + 1]) <= 0) {
            return x * 2;
        }
        return x * 2 + 1;
    }

    /**
     * Returns the number of items.
     *
     * @return the number of items
     */
    @Override
    public int size() {
        return items.size();
    }

    /**
     * Sets the priority of the given item to the given value.
     *
     * @param item the item
     * @param priority the priority
     * @throws NoSuchElementException if {@code item} does not exist
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new IllegalArgumentException("argument to changePriority() does not exist");
        }
        int i = items.get(item);
        double oldPriority = heap[i].getPriority();
        heap[i].setPriority(priority);
        if (priority > oldPriority) {
            sink(i);
        }
        else {
            swim(i);
        }
    }

    public T[] getItems() {
        T[] i = (T []) new Object[size()];
        return items.keySet().toArray(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for (int i = 1; i <= size(); i++) {
            sb.append(heap[i].getItem() + " ");
        }
        return sb.toString();
    }
}
