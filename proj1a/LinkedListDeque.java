/** Linked List Deque for Generic Types
  * arthur: Hsingyi Lin
  * date: 09/21/2019
  */

public class LinkedListDeque<T> {

    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node() {
            item = null;
            prev = null;
            next = null;
        }

        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node();
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /** Create a copy of the given deque. */
    public LinkedListDeque(LinkedListDeque other) {
        sentinel = new Node();
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;

        for (int i = 0; i < other.size(); i++) {
            addLast((T)other.get(i));
        }
    }

    /** Add an item of type T to the front of the deque. */
    public void addFirst(T item) {
        Node first = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = first;
        sentinel.next = first;
        size++;
    }

    /** Add an item of type T to the back of the deque. */
    public void addLast(T item) {
        Node last = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = last;
        sentinel.prev = last;
        size++;
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
        Node ptr = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(ptr.item + " ");
            ptr = ptr.next;
        }
        System.out.println();
    }

    /** Remove and return the item at the front of the deque. */
    public T removeFirst() {
        size = Math.min(0, --size);
        Node first = sentinel.next;
        sentinel.next = first.next;
        sentinel.next.prev = sentinel;
        return first.item;
    }

    /** Remove and return the item at the back of the deque. */
    public T removeLast() {
        size = Math.min(0, --size);
        Node last = sentinel.prev;
        sentinel.prev = last.prev;
        sentinel.prev.next = sentinel;
        return last.item;
    }

    /** Gets the item at the given index. */
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node ptr = sentinel;
        for (int i = 0; i < index; i++) {
            ptr = ptr.next;
        }
        return ptr.next.item;
    }

    /** Gets the item at the given index (recursively). */
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelper(index, sentinel.next);
    }

    /** Helper method for getRecursive. */
    private T getRecursiveHelper(int index, Node start) {
        if (index == 0) {
            return start.item;
        }
        return getRecursiveHelper(index - 1, start.next);
    }
}
