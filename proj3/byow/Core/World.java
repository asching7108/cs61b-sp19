package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

/**
 * Randomly generates a 2d world of rooms and hallways with the given random seed.
 *
 *  * At least some rooms should be rectangular.
 *  * At least some hallways should include turns (or straight hallways that intersect).
 *  * The world should contain a random number of rooms and hallways.
 *  * The locations of the rooms and hallways should be random.
 *  * The width and height of rooms should be random.
 *  * The length of hallways should be random.
 *  * Rooms and hallways must have walls that are visually distinct from floors.
 *  * Rooms and hallways should be connected.
 *
 */
public class World {
    private static final int WORLD_WIDTH = 60;
    private static final int WORLD_HEIGHT = 40;
    private static final int LEAF_MIN = 20;
    private static final int LEAF_MAX = 30;

    private Random random;     // random seed
    private TETile[][] world;  // 2d world of TETiles
    private BSPTree bsp;       // BSPTree that stores rooms and hallways

    /**
     * Generates the world with the given seed. Randomly decides the number of
     * potential rooms (i.e. BSPTree leaves), initializes the BSPTree, and
     * generates the 2D world of TETiles of connected rooms and hallways.
     *
     * @param seed the random seed
     */
    World(int seed) {
        random = new Random(seed);
        TERenderer ter = new TERenderer();
        ter.initialize(WORLD_WIDTH, WORLD_HEIGHT);
        world = new TETile[WORLD_WIDTH][WORLD_HEIGHT];
        initializeWorld();
        int leafNum = RandomUtils.uniform(random, LEAF_MAX - LEAF_MIN + 1) + LEAF_MIN;
        bsp = new BSPTree(WORLD_WIDTH, WORLD_HEIGHT, leafNum, random);
        generateWorld();
        ter.renderFrame(world);
    }

    /**
     * Represents the x and y value of a tile on the 2D world.
     */
    static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Initializes the world with TETile of NOTHING.
     */
    private void initializeWorld() {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Generate the world with rooms and hallways.
     */
    private void generateWorld() {
        List<Room> rooms = bsp.rooms();
        for (Room r : rooms) {
            addRoom(r);
        }
        List<Room> hallways = bsp.hallways();
        for (Room h : hallways) {
            addRoom(h);
        }
    }

    /**
     * Adds a room to the 2d world of TETiles.
     *
     * @param room the room
     */
    private void addRoom(Room room) {
        for (int y = 0; y < room.height(); y++) {
            addRowOfRoom(room, y);
        }
    }

    /**
     * Adds a row of room to the 2d world of TETiles. If the row is the first or the
     * last row, adds all TETiles of WALL. Otherwise, adds TETiles of WALL to the first
     * and the last tile and TETiles of FLOOR to the rest tiles.
     *
     * @param room the room
     * @param row the row index of the room
     */
    private void addRowOfRoom(Room room, int row) {
        int y = room.yOffset() + row;
        int xLast = room.xOffset() + room.width() - 1;
        if (row == 0 || row == room.height() - 1) {
            for (int x = room.xOffset(); x <= xLast; x++) {
                addTile(new Position(x, y), Tileset.WALL);
            }
            return;
        }
        addTile(new Position(room.xOffset(), y), Tileset.WALL);
        for (int x = room.xOffset() + 1; x < xLast; x++) {
            addTile(new Position(x, y), Tileset.FLOOR);
        }
        addTile(new Position(xLast, y), Tileset.WALL);
    }

    /**
     * Adds a tile to the 2d world with the given Position and TETile, except
     * when the current tile is FLOOR and the tile to be added is WALL to
     * prevent overlapping.
     *
     * @param p
     * @param t
     */
    private void addTile(Position p, TETile t) {
        if (!t.equals(Tileset.WALL) || !world[p.x][p.y].equals(Tileset.FLOOR)) {
            world[p.x][p.y] = t;
        }
    }

    public static void main(String[] args) {
        new World(2019);
    }
}
