public class Palindrome {
    /** Return a Deque where the characters appear
      * in the same order as in the given String.
      */
    public Deque<Character> wordToDeque(String word) {
        ArrayDeque<Character> wordDeque = new ArrayDeque<>();
        for (char ch : word.toCharArray()) {
            wordDeque.addLast(ch);
        }
        return wordDeque;
    }

    /** Return true if the given word is a palindrome,
      * and false otherwise.
      */
    public boolean isPalindrome(String word) {
        boolean output = true;
        Deque<Character> wordDeque = wordToDeque(word);
        for (int i = 0; i < wordDeque.size() / 2; i++) {
            if (wordDeque.removeFirst() != wordDeque.removeLast()) {
                output = false;
            }
        }
        return output;
    }

    /** Return true if the given word is a palindrome,
      * and false otherwise, based on the given CharacterComparator.
      */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        boolean output = true;
        Deque<Character> wordDeque = wordToDeque(word);
        for (int i = 0; i < wordDeque.size() / 2; i++) {
            if (!cc.equalChars(wordDeque.removeFirst(), wordDeque.removeLast())) {
                output = false;
            }
        }
        return output;
    }

}
