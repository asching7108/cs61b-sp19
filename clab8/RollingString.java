import java.util.LinkedList;

/**
 * A String-like class that allows users to add and remove characters in the String
 * in constant time and have a constant-time hash function. Used for the Rabin-Karp
 * string-matching algorithm.
 * @author Hsingyi Lin
 * date    10/02/2019
 */

class RollingString{

    private LinkedList<Character> string;
    private int lastHashCode;
    private int lastRemoved;

    /**
     * Number of total possible int values a character can take on.
     * DO NOT CHANGE THIS.
     */
    static final int UNIQUECHARS = 128;

    /**
     * The prime base that we are using as our mod space. Happens to be 61B. :)
     * DO NOT CHANGE THIS.
     */
    static final int PRIMEBASE = 6113;

    /**
     * Initializes a RollingString with a current value of String s.
     * s must be the same length as the maximum length.
     */
    public RollingString(String s, int length) {
        assert(s.length() == length);
        string = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            string.add(s.charAt(i));
        }
        lastRemoved = -1;
        lastHashCode = -1;
    }

    /**
     * Adds a character to the back of the stored "string" and 
     * removes the first character of the "string". 
     * Should be a constant-time operation.
     */
    public void addChar(char c) {
        lastHashCode = hashCode();
        lastRemoved = string.remove();
        string.add(c);
    }


    /**
     * Returns the "string" stored in this RollingString, i.e. materializes
     * the String. Should take linear time in the number of characters in
     * the string.
     */
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (Character c : string) {
            strb.append(c);
        }
        return strb.toString();
    }

    /**
     * Returns the fixed length of the stored "string".
     * Should be a constant-time operation.
     */
    public int length() {
        return string.size();
    }


    /**
     * Checks if two RollingStrings are equal.
     * Two RollingStrings are equal if they have the same characters in the same
     * order, i.e. their materialized strings are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        RollingString other = (RollingString) o;
        if (length() != other.length()) {
            return false;
        }
        for (int i = 0; i < string.size(); i++) {
            if (!string.get(i).equals(other.string.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the hashcode of the stored "string".
     * Should take constant time.
     */
    @Override
    public int hashCode() {
        if (lastHashCode == -1) {
            int h = 0;
            for (Character c : string) {
                h *= UNIQUECHARS;
                h += (int) c;
            }
            h %= PRIMEBASE;
            return h;
        }
        int old = lastRemoved * (int) Math.pow(UNIQUECHARS, (string.size() - 1)) % PRIMEBASE;
        int temp = ((lastHashCode + PRIMEBASE - old) * UNIQUECHARS) + (int) string.peekLast();
        return temp % PRIMEBASE;
    }
}
