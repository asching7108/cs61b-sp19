import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

/**
 * Objectives:
 * Separate the party-goers into three groups, again with the restriction that
 * no two members of the same group are enemies.
 * Test source:
 * https://bloggg-1254259681.cos.na-siliconvalley.myqcloud.com/b6cd9.png
 */

public class SeparableEnemySolverTestsThree {

    @Test
    public void test1() {
        Graph g = new Graph();
        g.connect("A", "B");
        g.connect("B", "C");
        SeparableEnemySolver solver = new SeparableEnemySolver(g);
        assertEquals(true, solver.isSeparableThree());
    }

    @Test
    public void test2() {
        Graph g = new Graph();
        g.connect("A", "B");
        g.connect("B", "C");
        g.connect("A", "C");
        SeparableEnemySolver solver = new SeparableEnemySolver(g);
        assertEquals(true, solver.isSeparableThree());
    }

    @Test
    public void test3() {
        Graph g = new Graph();
        g.connect("A", "B");
        g.connect("B", "C");
        g.connect("C", "D");
        g.connect("A", "D");
        SeparableEnemySolver solver = new SeparableEnemySolver(g);
        assertEquals(true, solver.isSeparableThree());
    }

    @Test
    public void test4() {
        Graph g = new Graph();
        g.connect("A", "B");
        g.connect("B", "C");
        g.connect("C", "D");
        g.connect("A", "D");
        g.connect("A", "C");
        SeparableEnemySolver solver = new SeparableEnemySolver(g);
        assertEquals(true, solver.isSeparableThree());
    }

    @Test
    public void test5() {
        Graph g = new Graph();
        g.connect("A", "B");
        g.connect("B", "C");
        g.connect("C", "D");
        g.connect("A", "D");
        g.connect("B", "D");
        SeparableEnemySolver solver = new SeparableEnemySolver(g);
        assertEquals(true, solver.isSeparableThree());
    }

    @Test
    public void test6() {
        Graph g = new Graph();
        g.connect("A", "B");
        g.connect("B", "C");
        g.connect("C", "D");
        g.connect("A", "D");
        g.connect("A", "C");
        g.connect("B", "D");
        SeparableEnemySolver solver = new SeparableEnemySolver(g);
        assertEquals(false, solver.isSeparableThree());
    }

    @Test
    public void test7() {
        Graph g = new Graph();
        g.connect("A", "B");
        g.connect("B", "C");
        g.connect("A", "C");
        g.connect("A", "D");
        g.connect("C", "D");
        g.connect("C", "E");
        g.connect("D", "E");
        g.connect("C", "F");
        g.connect("E", "F");
        g.connect("B", "F");
        SeparableEnemySolver solver = new SeparableEnemySolver(g);
        assertEquals(false, solver.isSeparableThree());
    }

}
