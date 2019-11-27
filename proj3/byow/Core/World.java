package byow.Core;

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
 * @author Hsingyi Lin
 */
public class World {
    private static final int LEAF_MIN = 15;
    private static final int LEAF_MAX = 30;

    private Random random;     // random seed
    private TETile[][] world;  // 2d world of TETiles
    private BSPTree bsp;       // BSPTree that stores rooms and hallways
    private Position player;   // Position of the player
    private Position treasure; // Position of the treasure

    /**
     * Generates the world with the given seed. Randomly decides the number of
     * potential rooms (i.e. BSPTree leaves), initializes the BSPTree, and
     * generates the 2D world of TETiles of connected rooms and hallways.
     *
     * @param seed the random seed
     */
    World(long seed, int width, int height) {
        random = new Random(seed);
        world = new TETile[width][height - 3];
        initializeWorld();
        int leafNum = RandomUtils.uniform(random, LEAF_MAX - LEAF_MIN + 1) + LEAF_MIN;
        bsp = new BSPTree(width, height - 3, leafNum, random);
        createPlayerAndTreasure();
        generateWorld();
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
        addTile(player, Tileset.AVATAR);
        addTile(treasure, Tileset.FLOWER);
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
     * @param p the position in the world to be added
     * @param t the TETile to be added
     */
    private void addTile(Position p, TETile t) {
        if (!t.equals(Tileset.WALL) || !world[p.x][p.y].equals(Tileset.FLOOR)) {
            world[p.x][p.y] = t;
        }
    }

    /**
     * Creates player and treasure in different random rooms.
     */
    private void createPlayerAndTreasure() {
        int roomIndex = RandomUtils.uniform(random, bsp.rooms().size());
        Room r = bsp.rooms().get(roomIndex);
        player = new Position(r.xOffset() + r.width() / 2, r.yOffset() + r.height() / 2);
        int roomIndex2 = RandomUtils.uniform(random, bsp.rooms().size());
        while (roomIndex2 == roomIndex) {
            roomIndex2 = RandomUtils.uniform(random, bsp.rooms().size());
        }
        r = bsp.rooms().get(roomIndex2);
        treasure = new Position(r.xOffset() + r.width() / 2, r.yOffset() + r.height() / 2);
    }

    /**
     * Moves the player to the adjacent position it's facing to and returns the game status:
     * if the player encounters the treasure, the user wins;
     * if the player encounters its previous track, the user loses;
     * otherwise the game continues.
     *
     * @param d the direction of the player
     * @return the game status
     */
    public Engine.Status movePlayer(Engine.Direction d) {
        Position target = target(d);
        TETile t = world[target.x][target.y];
        if (!t.equals(Tileset.WALL)) {
            addTile(player, Tileset.TRACK);
            addTile(target, Tileset.AVATAR);
            player = target;
            if (t.equals(Tileset.FLOWER)) {
                return Engine.Status.WIN;
            }
            if (t.equals(Tileset.TRACK)) {
                return Engine.Status.LOSE;
            }
        }
        return Engine.Status.PLAY;
    }

    /**
     * Returns the target position based on the player's current position and direction.
     *
     * @param d the direction of the player
     * @return the target position
     */
    private Position target(Engine.Direction d) {
        switch(d) {
            case UP: return new Position(player.x, player.y + 1);
            case RIGHT: return new Position(player.x + 1, player.y);
            case DOWN: return new Position(player.x, player.y - 1);
            case LEFT: return new Position(player.x - 1, player.y);
            default: return player;
        }
    }

    /**
     * Returns the 2d array of TETiles.
     *
     * @return the 2d array of TETiles.
     */
    public TETile[][] worldFrame() { return world; }

    /**
     * Returns the position of the player.
     *
     * @return the position of the player.
     */
    public Position getPlayer() { return player; }

    /**
     * Returns the position of the treasure.
     *
     * @return the position of the treasure.
     */
    public Position getTreasure() { return treasure; }

}
