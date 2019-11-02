package byow.Core;

import byow.SaveDemo.Editor;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private static final int WIDTH = 60;
    private static final int HEIGHT = 40;
    public enum Direction {UP, RIGHT, DOWN, LEFT};

    private StringBuilder inputs;

    private int status;
    private StringBuilder seedToBe;
    private long seed;
    private World world;

    public Engine() {
        inputs = new StringBuilder("");
        status = 0;
        seedToBe = new StringBuilder("");
        seed = -1;
        world = null;
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        initialize();
        drawMenu();
        InputSource inputSource = new KeyboardInputSource();
        while (status != 2) {
            parseMenuChoice(inputSource);
        }
        drawWorld();
        while (true) {
            parseMovement(inputSource, true);
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        InputSource inputSource = new StringInputSource(input);
        while (status != 2 && inputSource.possibleNextInput()) {
            parseMenuChoice(inputSource);
        }
        while (inputSource.possibleNextInput()) {
            parseMovement(inputSource, false);
        }
        return world.worldFrame();
    }

    private void parseMenuChoice(InputSource inputSource) {
        char ch = Character.toUpperCase(inputSource.getNextKey());
        if (status == 0 && ch == 'L') {
            load();
        }
        if (status == 0 && ch == 'N') {
            status = 1;
            inputs.append(ch);
        } else if (status == 1 && Character.isDigit(ch)) {
            seedToBe.append(ch);
            inputs.append(ch);
        } else if (status == 1 && ch == 'S') {
            seed = Integer.parseInt(seedToBe.toString());
            status = 2;
            inputs.append(ch);
            world = new World(seed, WIDTH, HEIGHT);
        } else if (ch == 'Q') {
            System.exit(0);
        }
    }

    private void parseMovement(InputSource inputSource, boolean draw) {
        char ch = Character.toUpperCase(inputSource.getNextKey());
        switch (ch) {
            case 'A': world.movePlayer(Direction.LEFT);
                      break;
            case 'W': world.movePlayer(Direction.UP);
                      break;
            case 'D': world.movePlayer(Direction.RIGHT);
                      break;
            case 'S': world.movePlayer(Direction.DOWN);
                      break;
            case ':': status = 3;
                      break;
            case 'Q': if (status == 3) {
                          save();
                      }
                      System.exit(0);
        }
        if (ch != ':') {
            inputs.append(ch);
        }
        if (draw) {
            drawWorld();
        }
    }

    private void initialize() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setPenColor(new Color(255, 255, 255));
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    private void drawMenu() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.setPenColor(Color.white);
        Font titleFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(titleFont);
        StdDraw.text(midWidth, HEIGHT - 10, "CS61B: FINAL GAME");
        Font subtitleFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(subtitleFont);
        StdDraw.text(midWidth, midHeight, "NEW GAME (N)");
        StdDraw.text(midWidth, midHeight - 2, "LOAD GAME (L)");
        StdDraw.text(midWidth, midHeight - 4, "QUIT (Q)");
        StdDraw.show();
    }

    private void drawWorld() {
        TETile[][] worldFrame = world.worldFrame();
        ter.renderFrame(worldFrame);
    }

    private void save() {
        File f = new File("./save_data.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(inputs.toString());
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private void load() {
        File f = new File("./save_data.txt");
        String loadInputs = null;
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                loadInputs = os.readObject().toString();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        interactWithInputString(loadInputs);
        drawWorld();
    }
}
