import edu.princeton.cs.algs4.Queue;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author Hsingyi Lin
 * date    10/08/2019
 */

public class TestSortAlgs {

    @Test  // doesn't use isSorted method
    public void testQuickSort() {
        Queue<String> tas = new Queue<>();
        tas.enqueue("Joe");
        tas.enqueue("Omar");
        tas.enqueue("Itai");
        Queue<String> sorted = QuickSort.quickSort(tas);
        String s = sorted.peek();
        for (String next : sorted) {
            assertFalse(next.compareTo(s) < 0);
        }
    }

    @Test  // doesn't use isSorted method
    public void testMergeSort() {
        Queue<String> tas = new Queue<>();
        tas.enqueue("Joe");
        tas.enqueue("Omar");
        tas.enqueue("Itai");
        Queue<String> sorted = MergeSort.mergeSort(tas);
        String s = sorted.peek();
        for (String next : sorted) {
            assertFalse(next.compareTo(s) < 0);
        }
    }

    @Test
    public void testMergeSort2() {
        Queue<Integer> tas = new Queue<>();
        tas.enqueue(32);
        tas.enqueue(15);
        tas.enqueue(2);
        tas.enqueue(17);
        tas.enqueue(19);
        tas.enqueue(26);
        tas.enqueue(41);
        tas.enqueue(17);
        tas.enqueue(17);
        Queue<Integer> sorted = MergeSort.mergeSort(tas);
        assertTrue(isSorted(sorted));
    }

    @Test
    public void testQuickSort2() {
        Queue<Integer> tas = new Queue<>();
        tas.enqueue(32);
        tas.enqueue(15);
        tas.enqueue(2);
        tas.enqueue(17);
        tas.enqueue(19);
        tas.enqueue(26);
        tas.enqueue(41);
        tas.enqueue(17);
        tas.enqueue(17);
        Queue<Integer> sorted = QuickSort.quickSort(tas);
        assertTrue(isSorted(sorted));
    }

    @Test
    public void testMergeSort3() {
        Random r = new Random();
        Queue<Integer> tas = new Queue<>();
        for (int i = 0; i < 10000; i++) {
            tas.enqueue(r.nextInt());
        }
        Queue<Integer> sorted = MergeSort.mergeSort(tas);
        assertTrue(isSorted(sorted));
    }

    @Test
    public void testQuickSort3() {
        Random r = new Random();
        Queue<Integer> tas = new Queue<>();
        for (int i = 0; i < 10000; i++) {
            tas.enqueue(r.nextInt());
        }
        Queue<Integer> sorted = QuickSort.quickSort(tas);
        assertTrue(isSorted(sorted));
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
