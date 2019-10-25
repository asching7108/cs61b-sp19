package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.lab6.UnionFind;

import java.util.*;

public class World {
    private static final int WORLD_WIDTH = 64;
    private static final int WORLD_HEIGHT = 32;
    private static final int ROOM_NUM_MIN = 11;
    private static final int ROOM_NUM_RANGE = 6;
    private static final int ROOM_SIZE_MIN = 4;
    private static final int ROOM_SIZE_RANGE = 6;
    private static final int HALLWAY_NUM_MIN = 11;
    private static final int HALLWAY_NUM_RANGE = 6;
    private static final int HALLWAY_SIZE_MIN = 3;
    private static final int HALLWAY_SIZE_RANGE = 20;

    private Random random;
    private TETile[][] world;
    private int[][] worldOfRooms;
    private UnionFind<Room> rooms;

    public World(long seed) {
        random = new Random(seed);
        TERenderer ter = new TERenderer();
        ter.initialize(WORLD_WIDTH, WORLD_HEIGHT);
        world = new TETile[WORLD_WIDTH][WORLD_HEIGHT];
        worldOfRooms = new int[WORLD_WIDTH][WORLD_HEIGHT];
        initializeWorld();
        rooms = generateRooms(random, world);
        generateHallways(world, random, rooms);
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

    class Room {
        int width;
        int height;
        int xOffset;
        int yOffset;

        Room(int w, int h, int x, int y) {
            width = w;
            height = h;
            xOffset = x;
            yOffset = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            Room other = (Room) o;
            return this.width == other.width && this.height == other.height
                    && this.xOffset == other.xOffset && this.yOffset == other.yOffset;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return ("start from " + "(" + xOffset + ", " + yOffset + ") "
                + "width=" + width + " height=" + height);
        }
    }

    private void initializeWorld() {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                world[x][y] = Tileset.NOTHING;
                worldOfRooms[x][y] = -1;
            }
        }
    }

    private UnionFind<Room> generateRooms(Random r, TETile[][] world) {
        UnionFind<Room> rooms = new UnionFind<>(10);
        int num = RandomUtils.uniform(r, ROOM_NUM_RANGE) + ROOM_NUM_MIN;
        while (num > 0) {
            int width = RandomUtils.uniform(r, ROOM_SIZE_RANGE) + ROOM_SIZE_MIN;
            int height = RandomUtils.uniform(r, ROOM_SIZE_RANGE) + ROOM_SIZE_MIN;
            int xOffset = RandomUtils.uniform(r, WORLD_WIDTH - width);
            int yOffset = RandomUtils.uniform(r, WORLD_HEIGHT - height);
            Room room = new Room(width, height, xOffset, yOffset);
            if (!isOverlap(world, room)) {
                int roomIdx = rooms.add(room);
                System.out.println(roomIdx);
                addRoom(world, room, roomIdx);
                num--;
            }
        }
        return rooms;
    }

    private void generateHallways(TETile[][] world, Random r, UnionFind rooms) {
        while (rooms.maxConnected() < 25) {
            int width = RandomUtils.uniform(r, HALLWAY_SIZE_RANGE) + HALLWAY_SIZE_MIN;
            int height = HALLWAY_SIZE_MIN;
            if (RandomUtils.uniform(r) < 0.5) {
                int temp = width;
                width = height;
                height = temp;
            }
            int xOffset = RandomUtils.uniform(r, WORLD_WIDTH - width);
            int yOffset = RandomUtils.uniform(r, WORLD_HEIGHT - height);
            Room room = new Room(width, height, xOffset, yOffset);

            int roomIdx = rooms.add(room);
            System.out.println(roomIdx);
            HashSet<Integer> overlapRoom = (HashSet) overlapRooms(room);
            if (overlapRoom.size() > 0) {
                addRoom(world, room, roomIdx);
                for (int idx : overlapRoom) {
                    rooms.union(roomIdx, idx);
                }
            }
        }
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.connectedItems(i) < 25) {
                removeRoom((Room) rooms.getItem(i));
                System.out.println(i);
            }
        }
    }

    private void removeRoom(Room room) {
        for (int y = 0; y < room.height; y++) {
            for (int x = 0; x < room.width; x++) {
                world[room.xOffset + x][room.yOffset + y] = Tileset.NOTHING;
                worldOfRooms[room.xOffset + x][room.yOffset + y] = -1;
            }
        }
    }

    private boolean isOverlap(TETile[][] world, Room room) {
        for (int y = 0; y < room.height; y++) {
            for (int x = 0; x < room.width; x++) {
                if (world[room.xOffset + x][room.yOffset + y].equals(Tileset.FLOOR)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addRoom(TETile[][] world, Room room, int roomIdx) {
        for (int y = 0; y < room.height; y++) {
            addRowOfRoom(world, room, roomIdx, y);
        }
    }

    private void addRowOfRoom(TETile[][] world, Room room, int roomIdx, int row) {
        int y = room.yOffset + row;
        if (row == 0 || row == room.height - 1) {
            for (int x = 0; x < room.width; x++) {
                addTile(world, new Position(room.xOffset + x, y), Tileset.WALL, roomIdx);
            }
            return;
        }
        addTile(world, new Position(room.xOffset, y), Tileset.WALL, roomIdx);
        for (int x = 1; x < room.width - 1; x++) {
            addTile(world, new Position(room.xOffset + x, y), Tileset.FLOOR, roomIdx);
        }
        addTile(world, new Position(room.xOffset + room.width - 1, y), Tileset.WALL, roomIdx);
    }

    private void addTile(TETile[][] world, Position p, TETile t, int roomIdx) {
        if (world[p.x][p.y].equals(Tileset.NOTHING)) {
            worldOfRooms[p.x][p.y] = roomIdx;
        }
        if (!t.equals(Tileset.WALL) || !world[p.x][p.y].equals(Tileset.FLOOR)) {
            world[p.x][p.y] = t;
        }
    }

    private Collection<Integer> overlapRooms(Room room) {
        HashSet<Integer> res = new HashSet<>();
        for (int x = 0; x < room.width; x++) {
            for (int y = 0; y < room.height; y++) {
                if (world[room.xOffset + x][room.yOffset + y] == Tileset.FLOOR) {
                    res.add(worldOfRooms[room.xOffset + x][room.yOffset + y]);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        new World(2019);
    }
}
