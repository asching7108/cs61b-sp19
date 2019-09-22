/** Array Deque for Generic Types
 * arthur: Hsingyi Lin
 * date: 09/21/2019
 */

public class ArrayDeque<T> {
    public static final int RFACTOR = 2;
    public static final double R = 0.25;

    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T []) new Object[8];
        nextFirst = 0;
        nextLast = 1;
        size = 0;
    }

    /** Create a copy of the given deque. */
    public ArrayDeque(ArrayDeque other) {
        items = (T []) new Object[other.size()];
        for (int i = 0; i < other.size(); i++) {
            items[i] = (T) other.get(i);
        }
        nextFirst = size - 1;
        nextLast = 0;
        size = other.size();
    }

    private int getIndex(int rawIndex) {
        if (rawIndex < 0) {
            return rawIndex % items.length + items.length;
        }
        return rawIndex % items.length;
    }

    /** Add an item of type T to the front of the deque. */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[nextFirst] = item;
        nextFirst = getIndex(nextFirst - 1);
        size++;
    }

    /** Add an item of type T to the back of the deque. */
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[nextLast] = item;
        nextLast = getIndex(nextLast + 1);
        size++;
    }

    /** Expand the array size to RFACTOR times. */
    private void resize(int newSize) {
        T[] newItems = (T []) new Object[newSize];
        for (int i = 0; i < size; i++) {
            newItems[i] = get(i);
        }
        items = newItems;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    /** Return true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Return the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Print the items in the deque from first to last,
     * separated by a space. Print a new line at the end. */
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items[getIndex(nextFirst + i + 1)] + " ");
        }
        System.out.println();
    }

    /** Remove and return the item at the front of the deque. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        nextFirst = getIndex(nextFirst + 1);
        T first = items[nextFirst];
        items[nextFirst] = null;
        size--;
        if ((double)size / items.length < R) {
            resize(items.length / 2);
        }
        return first;
    }

    /** Remove and return the item at the back of the deque. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        double ratio = (double)size / items.length;
        nextLast = getIndex(nextLast - 1);
        T last = items[nextLast];
        items[nextLast] = null;
        size--;
        if ((double)size / items.length < R) {
            resize(items.length / 2);
        }
        return last;
    }

    /** Gets the item at the given index. */
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return items[getIndex(nextFirst + index + 1)];
    }

}
