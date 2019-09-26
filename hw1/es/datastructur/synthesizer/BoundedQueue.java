package es.datastructur.synthesizer;
import java.util.Iterator;

public interface BoundedQueue<T> extends Iterable<T> {
    int capacity();     // return size of the buffer
    int fillCount();    // return number of items currently in the buffer
    void enqueue(T x);  // add item x to the end
    T dequeue();        // delete and return item from the front
    T peek();           // return (but do not delete) item from the front

    Iterator<T> iterator();

    /**
     * Return true is the queue is empty, otherwise return false.
     */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /**
     * Return true if the queue is full, otherwise return false.
     */
    default boolean isFull() {
        return capacity() == fillCount();
    }
}
