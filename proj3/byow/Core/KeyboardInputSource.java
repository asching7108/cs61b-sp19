package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

/**
 * Allows interacting with the keyboard inputs.
 * With reference to the example class in InputDemo.
 *
 * @author Hsingyi Lin
 */
public class KeyboardInputSource implements InputSource {
    @Override
    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                return Character.toUpperCase(StdDraw.nextKeyTyped());
            }
        }
    }

    @Override
    public boolean possibleNextInput() {
        return true;
    }
}
