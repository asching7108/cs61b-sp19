package byow.Core;

/**
 * Represents a room of the 2d world.
 */
public class Room {
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    Room(int x, int y, int w, int h) {
        xOffset = x;
        yOffset = y;
        width = w;
        height = h;
    }

    public int xOffset() { return xOffset; }

    public int yOffset() { return yOffset; }

    public int width() { return width; }

    public int height() { return height; }

    @Override
    public String toString() {
        return ("start from " + "(" + xOffset + ", " + yOffset + ") "
                + "width=" + width + " height=" + height);
    }

}
