package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

/**
 * Contains the two methods that allow interacting with the system and run the game.
 *
 * @author Hsingyi Lin
 */
public class Engine {
    public enum Direction {UP, RIGHT, DOWN, LEFT};
    public enum Status {START, SEED, PLAY, WIN, LOSE};

    private static final int WIDTH = 60;
    private static final int HEIGHT = 43;
    private static final int COUNTDOWN = 5;
    private static final Font TITLE_FONT = new Font("Monaco", Font.BOLD, 30);
    private static final Font SUBTITLE_FONT = new Font("Monaco", Font.BOLD, 20);
    private static final Font REGULAR_FONT = new Font("Monaco", Font.BOLD, 16);

    private TERenderer ter;
    private StringBuilder inputs;
    private Status status;
    private StringBuilder seedToBe;
    private long seed;
    private World world;

    public Engine() {
        ter = new TERenderer();
    }

    /**
     * Method used for exploring a fresh world. This method handles all inputs.
     */
    public void interactWithKeyboard() {
        // Initialize StdDraw
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();
        ter.initialize(WIDTH, HEIGHT, 0, 2);

        // Restart to the main menu when a game ends.
        // Only exit program (directly) when the users enter "Q".
        while (true) {
            initialize();
            InputSource inputSource = new KeyboardInputSource();
            // Handles inputs from the main menu and the prompt menu
            drawMenu();
            while (status != Status.PLAY) {
                parseMenuChoice(inputSource, true);
            }
            // Handles inputs from the game
            drawNewWorld();
            while (status == Status.PLAY) {
                parseMovement(inputSource, true);
            }
            // Let users press any key to continue
            inputSource.getNextKey();
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
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        initialize();
        InputSource inputSource = new StringInputSource(input);
        while (status != Status.PLAY && inputSource.possibleNextInput()) {
            parseMenuChoice(inputSource, false);
        }
        while (status == Status.PLAY && inputSource.possibleNextInput()) {
            parseMovement(inputSource, false);
        }
        if (world != null) {
            return world.worldFrame();
        }
        return null;
    }

    /**
     * Handles inputs from the main menu and the prompt menu.
     *
     * @param inputSource the inputSource
     * @param draw if it's needed to draw
     */
    private void parseMenuChoice(InputSource inputSource, boolean draw) {
        char ch = Character.toUpperCase(inputSource.getNextKey());
        // Loads the game from file
        if (status == Status.START && ch == 'L') {
            load();
        }
        // Prompts the users to enter the seed
        else if (status == Status.START && ch == 'N') {
            status = Status.SEED;
            inputs.append(ch);
            if (draw) {
                drawPrompt();
            }
        }
        // Displays the seedToBe the users has entered
        else if (status == Status.SEED && Character.isDigit(ch)) {
            seedToBe.append(ch);
            inputs.append(ch);
            if (draw) {
                drawPrompt();
            }
        }
        // Initializes the world with the seed
        else if (status == Status.SEED && ch == 'S') {
            seed = Long.parseLong(seedToBe.toString());
            status = Status.PLAY;
            inputs.append(ch);
            world = new World(seed, WIDTH, HEIGHT);
        }
        // Exit program
        else if (ch == 'Q') {
            System.exit(0);
        }
    }

    /**
     * Handles inputs from the game.
     *
     * @param inputSource the inputSource
     * @param draw if it's needed to draw
     */
    private void parseMovement(InputSource inputSource, boolean draw) {
        char ch = Character.toUpperCase(inputSource.getNextKey());
        switch (ch) {
            case 'A': status = world.movePlayer(Direction.LEFT);
                      inputs.append(ch);
                      break;
            case 'W': status = world.movePlayer(Direction.UP);
                      inputs.append(ch);
                      break;
            case 'D': status = world.movePlayer(Direction.RIGHT);
                      inputs.append(ch);
                      break;
            case 'S': status = world.movePlayer(Direction.DOWN);
                      inputs.append(ch);
                      break;
            case ':': save();
                      break;
            case 'Q': System.exit(0);
        }
        if (draw) {
            drawWorld();
            if (status == Status.WIN || status == Status.LOSE) {
                drawResult();
            }
        }
    }

    /**
     * Initializes all member variables and resets the game.
     */
    private void initialize() {
        inputs = new StringBuilder("");
        status = Status.START;
        seedToBe = new StringBuilder("");
        seed = -1;
        world = null;
    }

    /**
     * Draws the main menu.
     */
    private void drawMenu() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(TITLE_FONT);
        StdDraw.text(midWidth, HEIGHT - 10, "CS61B: FINAL GAME");
        StdDraw.setFont(SUBTITLE_FONT);
        StdDraw.text(midWidth, midHeight, "NEW GAME (N)");
        StdDraw.text(midWidth, midHeight - 2, "LOAD GAME (L)");
        StdDraw.text(midWidth, midHeight - 4, "QUIT (Q)");
        StdDraw.show();
    }

    /**
     * Draws the prompt menu that lets the users enter the seed.
     */
    private void drawPrompt() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        String input = inputs.toString();
        input = input.substring(input.indexOf('N') + 1);

        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(TITLE_FONT);
        StdDraw.text(midWidth, HEIGHT - 10, "NEW GAME");
        StdDraw.setFont(SUBTITLE_FONT);
        StdDraw.text(midWidth, midHeight, "Enter any number within 18 digits.");
        StdDraw.text(midWidth, midHeight - 2, "Then press \"S\".");
        StdDraw.setPenColor(Color.yellow);
        StdDraw.text(midWidth, midHeight - 4, input);
        StdDraw.show();
    }

    /**
     * Draws the game with 5 seconds of full map view.
     */
    private void drawNewWorld() {
        TETile[][] worldFrame = world.worldFrame();
        for (int i = COUNTDOWN; i > 0; i--) {
            ter.renderFrame(worldFrame);
            addInstruction();
            addHint();
            addCountdown(i);
            StdDraw.show();
            StdDraw.pause(1000);
        }
        drawWorld();
    }

    /**
     * Draws the game with a cross view.
     */
    private void drawWorld() {
        TETile[][] worldFrame = world.worldFrame();
        ter.renderRestrictedFrame(worldFrame, world.getPlayer(), world.getTreasure());
        addInstruction();
        addHint();
        StdDraw.show();
    }

    /**
     * Adds instructions to StdDraw.
     */
    private void addInstruction() {
        StdDraw.setFont(REGULAR_FONT);
        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(0, HEIGHT - 1, "A:LEFT W:UP D:RIGHT S:DOWN");
        StdDraw.textRight(WIDTH - 1, HEIGHT - 1, "\":\":SAVE Q:QUIT");
    }

    /**
     * Adds hints to StdDraw.
     */
    private void addHint() {
        StdDraw.setPenColor(Color.yellow);
        StdDraw.text(WIDTH / 2, 1,
                "Get to the treasure without overlapping your previous track, and you win!");
    }

    /**
     * Adds countdown messages to StdDraw.
     *
     * @param countDown the countdown in seconds
     */
    private void addCountdown(int countDown) {
        StdDraw.setFont(REGULAR_FONT);
        StdDraw.setPenColor(Color.yellow);
        StdDraw.text(WIDTH / 2, HEIGHT - 2, "Hide full map in " + countDown + " seconds...");
    }

    /**
     * Draws the game result with a full map view.
     */
    private void drawResult() {
        TETile[][] worldFrame = world.worldFrame();
        ter.renderFrame(worldFrame);
        addInstruction();
        StdDraw.setFont(REGULAR_FONT);
        if (status == Status.WIN) {
            StdDraw.setPenColor(Color.CYAN);
            StdDraw.text(WIDTH / 2, HEIGHT - 2, "You found the treasure! You win!");
        }
        else if (status == Status.LOSE) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.text(WIDTH / 2, HEIGHT - 2, "You stepped on your track! You lose!");
        }
        StdDraw.setPenColor(Color.yellow);
        StdDraw.text(WIDTH / 2, 1, "Press any key to continue");
        StdDraw.show();
    }

    /**
     * Saves the current inputs to a text file.
     */
    private void save() {
        File f = new File("./save_data.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(inputs.toString());
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /**
     * Reads the saved text file and handles the inputs in the file.
     */
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
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        interactWithInputString(loadInputs);
    }

}
