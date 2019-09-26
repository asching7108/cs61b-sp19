package es.datastructur.synthesizer;
import java.util.Iterator;

/** ArrayRingBuffer implements BoundedQueue interface,
 * but improves efficiency by using the "ring buffer"
 * data structure.
 * @Author Hsingyi Lin
 * date    09/25/2019
 */

public class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T []) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
    }

    /**
     * Return size of the buffer.
     */
    @Override
    public int capacity() {
        return rb.length;
    }

    /**
     * Return number of items currently in the buffer.
     */
    @Override
    public int fillCount() {
        return fillCount;
    }

    /**
     * Helper method that takes a parameter index which may
     * not be valid, and returns the valid index.
     */
    private int updateIndex(int in) {
        if (in >= capacity()) {
            return in - capacity();
        }
        return in;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last = updateIndex(last + 1);
        fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T item = rb[first];
        rb[first] = null;
        first = updateIndex(first + 1);
        fillCount--;
        return item;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return rb[first];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingIterator();
    }

    /**
     * Implementation of the iterator.
     */
    private class ArrayRingIterator implements Iterator<T> {
        private int pos;
        private int countFixed;
        private int firstFixed;

        public ArrayRingIterator() {
            pos = 0;
            countFixed = fillCount();
            firstFixed = first;
        }

        public boolean hasNext() {
            return pos < countFixed;
        }

        public T next() {
            T returnItem = rb[updateIndex(firstFixed + pos)];
            pos++;
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        ArrayRingBuffer other = (ArrayRingBuffer) o;
        if (this.capacity() != other.capacity()) {
            return false;
        }
        if (this.fillCount() != other.fillCount()) {
            return false;
        }
        boolean result = true;
        for (T item : this) {
            if (!item.equals(other.peek())) {
                result = false;
            }
            this.enqueue(this.dequeue());
            other.enqueue(other.dequeue());
        }
        return result;
    }

}
