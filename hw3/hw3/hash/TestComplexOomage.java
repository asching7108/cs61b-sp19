package hw3.hash;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /* added by Hsingyi Lin 09/30/2019. This test fails when ComplexOomage
     * uses 256 as base for its hashcode, passes with 257 instead. With a
     * 256 base, all ComplexOomages in the deadlyList has the same hashcode.
     */
    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();
        int f1 = StdRandom.uniform(0, 255);
        int f2 = StdRandom.uniform(0, 255);
        int f3 = StdRandom.uniform(0, 255);
        int f4 = StdRandom.uniform(0, 255);
        int N = 100;

        for (int i = 0; i < N; i += 1) {
            ArrayList<Integer> params = new ArrayList<>(10);
            for (int j = 0; j < 6; j += 1) {
                params.add(StdRandom.uniform(0, 255));
            }
            params.add(f1);
            params.add(f2);
            params.add(f3);
            params.add(f4);
            deadlyList.add(new ComplexOomage(params));
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
