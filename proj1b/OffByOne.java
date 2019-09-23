/** Off By One Character Comparator
 * author: Hsingyi Lin
 * date:   09/23/2019
 */

public class OffByOne implements CharacterComparator {
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == 1;
    }
}
