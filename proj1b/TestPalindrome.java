import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();
    static OffByOne obo = new OffByOne();
    OffByN offBy5 = new OffByN(5);
    OffByN offBy10 = new OffByN(10);

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertFalse(palindrome.isPalindrome("cat"));
        assertTrue(palindrome.isPalindrome("racecar"));
        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome("s"));
    }

    @Test
    public void testOffByOne() {
        assertFalse(obo.equalChars('a', 'e'));
        assertTrue(obo.equalChars('r', 'q'));
        assertTrue(obo.equalChars('q', 'r'));
    }

    @Test
    public void testIsPalindromeOffByOne() {
        assertFalse(palindrome.isPalindrome("cat", obo));
        assertFalse(palindrome.isPalindrome("racecar", obo));
        assertTrue(palindrome.isPalindrome("", obo));
        assertTrue(palindrome.isPalindrome("s", obo));
        assertTrue(palindrome.isPalindrome("flake", obo));
    }

    @Test
    public void testOffByN() {
        assertFalse(offBy5.equalChars('f', 'h'));
        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('f', 'a'));
    }

    @Test
    public void testIsPalindromeOffByN() {
        assertFalse(palindrome.isPalindrome("cat", offBy5));
        assertFalse(palindrome.isPalindrome("racecar", offBy5));
        assertTrue(palindrome.isPalindrome("", offBy5));
        assertTrue(palindrome.isPalindrome("s", offBy5));
        assertFalse(palindrome.isPalindrome("flake", offBy5));
        assertTrue(palindrome.isPalindrome("is", offBy10));
    }
}