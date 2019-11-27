package byow.Core;

/**
 * Allows interacting with the string argument as inputs.
 * With reference to the example class in InputDemo.
 *
 * @author Hsingyi Lin
 */
public class StringInputSource implements InputSource {
    private String input;
    private int index;

    public StringInputSource(String s) {
        index = 0;
        input = s;
    }

    @Override
    public char getNextKey() {
        if (input != null) {
            char returnChar = input.charAt(index);
            index += 1;
            return returnChar;
        }
        return ' ';
    }

    @Override
    public boolean possibleNextInput() {
        if (input != null) {
            return index < input.length();
        }
        return false;
    }
}
