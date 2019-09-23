/** This test program checks if the two deques generate
  * the same results, and prints the operations if not.
  * author: Hsingyi Lin
  * date:   09/23/2019
  */
import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void checkStudentArrayDeque() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ad1 = new ArrayDequeSolution<>();
        String message = "";

        for (int i = 0; i < 100; i += 1) {
            /* Randomly pick a method to execute on both deques. */
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (!ad1.isEmpty()
                    && numberBetweenZeroAndOne >= 0.8) {
                message += "removeFirst()\n";
                assertEquals(message, ad1.removeFirst(), sad1.removeFirst());
            } else if (!ad1.isEmpty()
                    && numberBetweenZeroAndOne >= 0.6
                    && numberBetweenZeroAndOne < 0.8) {
                message += "removeLast()\n";
                assertEquals(message, ad1.removeLast(), sad1.removeLast());
            } else if (numberBetweenZeroAndOne >= 0.3
                    && numberBetweenZeroAndOne < 0.6) {
                sad1.addFirst(i);
                ad1.addFirst(i);
                message += "addFirst(" + i + ")\n";
                assertEquals(message, ad1.get(0), sad1.get(0));
            }
            else {
                sad1.addLast(i);
                ad1.addLast(i);
                message += "addLast(" + i + ")\n";
                assertEquals(message, ad1.get(sad1.size() - 1), sad1.get(ad1.size() - 1));
            }
            message += "size()\n";
            assertEquals(message, ad1.size(), sad1.size());
        }
    }
}
