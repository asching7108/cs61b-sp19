package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class World {
    private static final int WORLD_WIDTH = 60;
    private static final int WORLD_HEIGHT = 40;

    private Random random;
    private TETile[][] world;
    private BSPTree bsp;


    World(int seed) {
        random = new Random(seed);
        TERenderer ter = new TERenderer();
        ter.initialize(WORLD_WIDTH, WORLD_HEIGHT);
        world = new TETile[WORLD_WIDTH][WORLD_HEIGHT];
        bsp = new BSPTree(WORLD_WIDTH, WORLD_HEIGHT, 30, random);
        initializeWorld();
        generateWorld();
        ter.renderFrame(world);
    }

    /**
     * Represents the x and y value of a tile on the 2D world.
     */
    class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


    private void initializeWorld() {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void generateWorld() {
        LinkedList<Room> rooms = (LinkedList) bsp.rooms();
        for (Room r : rooms) {
            System.out.println(r);
            addRoom(world, r);
        }
        LinkedList<Room> hallways = (LinkedList) bsp.hallways();
        for (Room r : hallways) {
            System.out.println(r);
            addRoom(world, r);
        }
    }

    private void addRoom(TETile[][] world, Room room) {
        for (int y = 0; y < room.height(); y++) {
            addRowOfRoom(world, room, y);
        }
    }

    private void addRowOfRoom(TETile[][] world, Room room, int row) {
        int y = room.yOffset() + row;
        if (row == 0 || row == room.height() - 1) {
            for (int x = 0; x < room.width(); x++) {
                addTile(world, new Position(room.xOffset() + x, y), Tileset.WALL);
            }
            return;
        }
        addTile(world, new Position(room.xOffset(), y), Tileset.WALL);
        for (int x = 1; x < room.width() - 1; x++) {
            addTile(world, new Position(room.xOffset() + x, y), Tileset.FLOOR);
        }
        addTile(world, new Position(room.xOffset() + room.width() - 1, y), Tileset.WALL);
    }

    private void addTile(TETile[][] world, Position p, TETile t) {
        if (!t.equals(Tileset.WALL) || !world[p.x][p.y].equals(Tileset.FLOOR)) {
            world[p.x][p.y] = t;
        }
    }

    public static void main(String[] args) {
        new World(2019);
    }
}
