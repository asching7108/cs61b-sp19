package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 *
 * @author Hsingyi Lin
 * date    10/16/2019
 */

public class HexWorld {
    private static final long SEED = 2019;
    private static final Random RANDOM = new Random(SEED);

    private static final int MAX_HEX_ROW = 9;
    private static final int MAX_HEX_COL = 5;

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

    /**
     * Generates the hex world of the given hex size.
     *
     * @param size the number of tiles per side of a hex
     */
    public HexWorld(int size) {
        // width and height are the max width and height  of all rows
        // and columns plus 2 empty tiles toward each border
        int width = size * 11 - 2;
        int height = size * 10 + 4;
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        TETile[][] hexWorld = initializeEmptyWorld(width, height);
        // draw each row of hexes
        for (int i = 0; i < MAX_HEX_ROW; i++) {
            drawRowOfHexes(hexWorld, size, i);
        }
        ter.renderFrame(hexWorld);
    }

    /**
     * Draws each row of hexes. There are 5 columns of hexes in total.
     * For the top and bottom row, there's only a hex in the middle column.
     * For all other rows, if the row index is 1, 3, 5 or 7, there are two
     * columns of hexes of index 1 and 3; if the row index is 2, 4 and 6,
     * there are three columns of hexes of index 0, 2 and 4.
     *
     * hex in the middle.
     * @param world the hex world
     * @param size the size
     * @param i the row index of the hex
     */
    private void drawRowOfHexes(TETile[][] world, int size, int i) {
        if (i == 0 || i == MAX_HEX_ROW - 1) {
            addHexagon(world, hexOffset(size, i, 2), size, randomTile());
            return;
        }
        for (int j = 0; j < MAX_HEX_COL; j++) {
            if ((i - j) % 2 == 0) {
                addHexagon(world, hexOffset(size, i, j), size, randomTile());
            }
        }
    }

    /**
     * Returns the Position of the bottom-left corner of the input hex.
     *
     * @param size the size
     * @param row the row index of the hex
     * @param col the column index of the hex
     * @return the Position of the bottom-left corner of the input hex
     */
    private Position hexOffset(int size, int row, int col) {
        return new Position(col * (size * 2 - 1) + 2, row * size + 2);
    }

    private TETile[][] initializeEmptyWorld(int width, int height) {
        TETile[][] hexWorld = new TETile[width][height];
        for (int x = 0; x < hexWorld.length; x++) {
            for (int y = 0; y < hexWorld[0].length; y++) {
                hexWorld[x][y] = Tileset.NOTHING;
            }
        }
        return hexWorld;
    }

    /**
     * Adds a hex to the 2d hex world.
     *
     * @param world the hex world
     * @param p the position of the bottom-left corner of the hex
     * @param s the size
     * @param t the tile
     */
    private void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        for (int y = 0; y < s * 2; y++) {
            int rWidth = hexRowWidth(s, y);
            int xOffset = hexRowOffset(s, y);
            addRow(world, new Position(p.x + xOffset, p.y + y), rWidth, t);
        }
    }

    /**
     * Adds a row of a hex in to the 2d hex world.
     *
     * @param world the hex world
     * @param p the left-most position of the row
     * @param rWidth the width of the row
     * @param t the tile
     */
    private void addRow(TETile[][] world, Position p, int rWidth, TETile t) {
        for (int x = p.x; x < p.x + rWidth; x++) {
            world[x][p.y] = t;
        }
    }

    /**
     * Returns the number of tiles of the given y offset. Note that
     * the hex is vertically symmetrical.
     *
     * @param size the size
     * @param y the vertical offset the row in a hex
     * @return the number of tiles of the given y offset
     */
    private int hexRowWidth(int size, int y) {
        int symmetryY = y;
        if (y >= size) {
            symmetryY = size * 2 - y - 1;
        }
        return size + 2 * symmetryY;
    }

    /**
     * Return the x offset of the given y offset. Note that the hex
     * is vertically symmetrical.
     *
     * @param size the size
     * @param y the vertical offset the row in a hex
     * @return the x offset
     */
    private int hexRowOffset(int size, int y) {
        int symmetryY = y;
        if (y >= size) {
            symmetryY = size * 2 - y - 1;
        }
        return size - symmetryY - 1;
    }

    /**
     * Picks a RANDOM tile with equal chance of being a tile of
     * grass, flower, desert, forest, or mountain.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.SAND;
            case 3: return Tileset.TREE;
            case 4: return Tileset.MOUNTAIN;
            default: return Tileset.FLOWER;
        }
    }

    public static void main(String[] args) {
        new HexWorld(4);
    }

}
