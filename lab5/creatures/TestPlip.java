package creatures;
import huglife.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;

/** Tests the plip class
 *  @author Hsingyi Lin
 *  date    09/24/2019
 */

public class TestPlip {

    @Test
    public void testBasics() {
        Plip p = new Plip(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(99, 255, 76), p.color());
        p.move();
        assertEquals(1.85, p.energy(), 0.01);
        p.move();
        assertEquals(1.70, p.energy(), 0.01);
        p.stay();
        assertEquals(1.90, p.energy(), 0.01);
        p.stay();
        assertEquals(2.00, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Plip p1 = new Plip(3);
        Plip p2 = p1.replicate();
        assertNotEquals(p1, p2);
        assertEquals(1.5, p1.energy(), 0.01);
        assertEquals(1.5, p2.energy(), 0.01);
    }

    @Test
    public void testChoose() {

        // No empty adjacent spaces; stay.
        Plip p = new Plip(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        p = new Plip(1.2);
        HashMap<Direction, Occupant> topEmpty = new HashMap<Direction, Occupant>();
        topEmpty.put(Direction.TOP, new Empty());
        topEmpty.put(Direction.BOTTOM, new Clorus());
        topEmpty.put(Direction.LEFT, new Impassible());
        topEmpty.put(Direction.RIGHT, new Impassible());

        actual = p.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        p = new Plip(1.2);
        HashMap<Direction, Occupant> allEmpty = new HashMap<Direction, Occupant>();
        allEmpty.put(Direction.TOP, new Empty());
        allEmpty.put(Direction.BOTTOM, new Empty());
        allEmpty.put(Direction.LEFT, new Empty());
        allEmpty.put(Direction.RIGHT, new Empty());

        actual = p.chooseAction(allEmpty);
        Action unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);


        // Energy < 1; stay.
        p = new Plip(.99);

        actual = p.chooseAction(allEmpty);
        expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // Energy < 1; stay.
        p = new Plip(.99);

        actual = p.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // Energy < 1 and any Cloruses, MOVE towards an empty
        // direction chosen at random. (Expected half passes and half fails)
        int actualPercentage = 0;
        for (int i = 0; i < 100; i++) {
            p = new Plip(0.99);
            HashMap<Direction, Occupant> ClorusAround = new HashMap<Direction, Occupant>();
            ClorusAround.put(Direction.TOP, new Clorus());
            ClorusAround.put(Direction.BOTTOM, new Clorus());
            ClorusAround.put(Direction.LEFT, new Clorus());
            ClorusAround.put(Direction.RIGHT, new Empty());

            actual = p.chooseAction(ClorusAround);
            expected = new Action(Action.ActionType.MOVE, Direction.RIGHT);
            if (expected.equals(actual)) {
                actualPercentage++;
            }
        }
            assertEquals(50, actualPercentage, 10);
    }
}
