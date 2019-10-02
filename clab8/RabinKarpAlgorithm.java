/**
 * @author Hsingyi Lin
 * date    10/02/2019
 */

public class RabinKarpAlgorithm {

    /**
     * This algorithm returns the starting index of the matching substring.
     * This method will return -1 if no matching substring is found, or if the input is invalid.
     */
    public static int rabinKarp(String input, String pattern) {
        int m = pattern.length();
        RollingString p = new RollingString(pattern, m);
        RollingString rs = new RollingString(input.substring(0, m), m);
        for (int i = 0; i < input.length() - m; i++) {
            if (p.hashCode() == rs.hashCode()) {
                if (p.equals(rs)) {
                    return i;
                }
            }
            rs.addChar(input.charAt(Math.min(i + m, input.length() - 1)));
        }
        return -1;
    }

}
