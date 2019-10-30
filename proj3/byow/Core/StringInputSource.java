package byow.Core;

public class StringInputSource implements InputSource {
    private String input;
    private int index;

    public StringInputSource(String s) {
        index = 0;
        input = s;
    }

    @Override
    public char getNextKey() {
        char returnChar = input.charAt(index);
        index += 1;
        return returnChar;
    }

    @Override
    public boolean possibleNextInput() {
        return index < input.length();
    }
}
