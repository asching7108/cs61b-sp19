/** Off By N Character Comparator
 * author: Hsingyi Lin
 * date:   09/23/2019
 */

public class OffByN implements CharacterComparator {
    private int N;
    public OffByN(int n) {
        N = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == N;
    }
}
