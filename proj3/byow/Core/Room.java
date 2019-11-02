package byow.Core;

/**
 * Represents a room of the 2d world.
 *
 * @author Hsingyi Lin
 */
public class Room {
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private Position player;

    Room(int x, int y, int w, int h) {
        xOffset = x;
        yOffset = y;
        width = w;
        height = h;
        player = null;
    }

    public int xOffset() { return xOffset; }

    public int yOffset() { return yOffset; }

    public int width() { return width; }

    public int height() { return height; }

    public Position getPlayer() { return player; }

    public void setPlayer(int x, int y) {
        player = new Position(x, y);
    }

    @Override
    public String toString() {
        return ("start from " + "(" + xOffset + ", " + yOffset + ") "
                + "width=" + width + " height=" + height);
    }

}
