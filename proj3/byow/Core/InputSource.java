package byow.Core;

/**
 * Contains the two methods that allow interacting with the inputs.
 * With reference to the example interface in InputDemo.
 *
 * @author Hsingyi Lin
 */
public interface InputSource {
    public char getNextKey();
    public boolean possibleNextInput();
}
