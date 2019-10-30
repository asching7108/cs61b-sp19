package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

public class testEngine {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;

    public static void main (String[] args) {
        Engine engine = new Engine();
        TERenderer ter = new TERenderer();
        TETile[][] worldFrame = engine.interactWithInputString("n201:q");
        worldFrame = engine.interactWithInputString("l9s");
        if (worldFrame != null) {
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(worldFrame);
        }
    }
}
