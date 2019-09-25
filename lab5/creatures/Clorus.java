package creatures;

import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * An implementation of a fierce predator.
 *
 * @author Hsingyi Lin
 * date    09/24/2019
 */

public class Clorus extends Creature {
    /**
     * constants
     */
    private static final double MOVE_LOSS = 0.03;
    private static final double STAY_LOSS = 0.01;

    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;

    /**
     * creates clorus with energy equal to E.
     */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /**
     * creates a clorus with energy equal to 1.
     */
    public Clorus() {
        this(1);
    }

    /**
     * Should return a color with red = 34, green = 0, blue = 231.
     */
    public Color color() {
        return color(34, 0, 231);
    }

    /**
     * Clorus gains the energy of the creature it attacks.
     */
    public void attack(Creature c) {
        energy += c.energy();
    }

    /**
     * Plips should lose 0.03 units of energy when moving.
     */
    public void move() {
        energy -= MOVE_LOSS;
    }


    /**
     * Plips gain 0.01 energy when staying due to photosynthesis.
     */
    public void stay() {
        energy -= STAY_LOSS;
    }

    /**
     * Cloruses and their offspring each get 50% of the energy, with none
     * lost to the process. Returns a baby Clorus.
     */
    public Clorus replicate() {
        Clorus offspring = new Clorus(energy / 2);
        energy /= 2;
        return offspring;
    }

    /**
     * Clorus behavioral rules:
     * 1. If no empty adjacent spaces, STAY.
     * 2. Otherwise, if any Plips, ATTACK one randomly.
     * 3. Otherwise, if energy >= 1, REPLICATE towards an empty direction
     * chosen at random.
     * 4. Otherwise, MOVE randomly.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> plipNeighbors = new ArrayDeque<>();
        boolean anyPlip = false;
        for (Direction drct : neighbors.keySet()) {
            if (neighbors.get(drct).name() == "empty") {
                emptyNeighbors.add(drct);
            } else if (neighbors.get(drct).name() == "plip") {
                anyPlip = true;
                plipNeighbors.add(drct);
            }
        }

        // Rule 1
        if (emptyNeighbors.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        }

        // Rule 2
        if (anyPlip) {
            return new Action(Action.ActionType.ATTACK,
                    HugLifeUtils.randomEntry(plipNeighbors));
        }

        // Rule 3
        if (energy >= 1.0) {
            return new Action(Action.ActionType.REPLICATE,
                    HugLifeUtils.randomEntry(emptyNeighbors));
        }

        // Rule 4
        return new Action(Action.ActionType.MOVE,
                HugLifeUtils.randomEntry(emptyNeighbors));
    }
}
