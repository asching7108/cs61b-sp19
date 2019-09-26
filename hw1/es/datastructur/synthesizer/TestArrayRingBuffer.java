package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(4);
        assertEquals(4, arb.capacity());
        assertTrue(arb.isEmpty());
        arb.enqueue(9.3);    // 9.3
        arb.enqueue(15.1);   // 9.3  15.1
        arb.enqueue(31.2);   // 9.3  15.1  31.2
        assertFalse(arb.isFull());
        arb.enqueue(-3.1);   // 9.3  15.1  31.2  -3.1
        assertTrue(arb.isFull());
        assertEquals(9.3, arb.dequeue());
        assertEquals(3, arb.fillCount());
        assertEquals(15.1, arb.peek());
        assertEquals(3, arb.fillCount());
    }

    @Test
    public void testEquality() {
        ArrayRingBuffer<Integer> a1 = new ArrayRingBuffer<>(3);
        ArrayRingBuffer<Integer> a2 = new ArrayRingBuffer<>(2);
        assertFalse(a1.equals(a2));
        a1.enqueue(2);
        a1.enqueue(4);
        a1.enqueue(8);
        a2 = new ArrayRingBuffer<>(3);
        a2.enqueue(2);
        a2.enqueue(4);
        assertFalse(a1.equals(a2));
        a2.enqueue(8);
        assertTrue(a1.equals(a2));
        a2.dequeue();
        a2.dequeue();
        a2.dequeue();
        a2.enqueue(2);
        a2.enqueue(4);
        a2.enqueue(6);
        assertFalse(a1.equals(a2));
    }

}
