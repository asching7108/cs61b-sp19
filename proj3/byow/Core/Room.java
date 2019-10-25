package byow.Core;

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

}
