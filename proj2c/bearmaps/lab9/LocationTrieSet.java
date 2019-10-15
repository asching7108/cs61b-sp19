package bearmaps.lab9;

import bearmaps.hw4.streetmap.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@code MyTrieSet} class using a Trie as its core data structure. Allows finding keys
 * with given prefix application. Modified from the original TrieSet in lab9 to construct
 * the trie with clean string, store the full string and ids in the key node.
 *
 * @author Hsingyi Lin
 * date    10/14/2019
 */

public class LocationTrieSet {

    private TrieNode root;
    private int size;

    /**
     * Stores a character, key identification, and a HashMap for the following
     * characters.
     */
    private static class TrieNode {
        private char ch;
        private boolean isKey;
        private String fullkey;
        private ArrayList<Node> nodes;
        private HashMap<Character, TrieNode> next;
        private TrieNode(char c, boolean k) {
            ch = c;
            isKey = k;
            fullkey = null;
            nodes = new ArrayList<>();
            next = new HashMap<>();
        }
    }

    /**
     * Initializes the root node with an arbitrary character.
     */
    public LocationTrieSet() {
        root = new TrieNode('r', false);
        size = 0;
    }

    /**
     * Clear all following nodes besides the root.
     */
    public void clear() {
        root.next.clear();
        size = 0;
    }

    /**
     * Return {@code true} if this trie set contains the given {@code key}.
     *
     * @param key the key string
     * @return {@code true} if this trie set contains the given {@code key} and
     *         {@code false} otherwise.
     * @throws IllegalArgumentException if {@code key} is {@code null} or size is 0
     */
    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            throw new IllegalArgumentException("argument to contains() is null or size is 0");
        }
        TrieNode curr = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (!curr.next.containsKey(ch)) {
                return false;
            }
            curr = curr.next.get(ch);
        }
        return true;
    }

    /**
     * Inserts the key into the trie set.
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null} or size is 0
     */
    public void add(String key, Node node) {
        if (key == null || key.length() < 1) {
            throw new IllegalArgumentException("argument to add() is null or size is 0");
        }
        String full = key;
        key = cleanString(key);
        if (key.length() < 1) {
            return;
        }
        addHelper(root, key, full, node);
    }

    /**
     * Helper function to recursively match and insert characters of the given key.
     *
     * @param n the node
     * @param k the key
     */
    private void addHelper(TrieNode n, String k, String f, Node node) {
        char ch = k.charAt(0);
        if (!n.next.containsKey(ch)) {
            n.next.put(ch, new TrieNode(ch, false));
            size++;
        }
        TrieNode nextTrieNode = n.next.get(ch);
        if (k.length() == 1) {
            nextTrieNode.isKey = true;
            nextTrieNode.fullkey = f;
            nextTrieNode.nodes.add(node);
        }
        else {
            addHelper(nextTrieNode, k.substring(1), f, node);
        }
    }

    /**
     * Returns all keys in the trie set that match with the prefix.
     *
     * @param prefix the prefix string
     * @return the list of keys with the given prefix
     * @throws IllegalArgumentException if {@code key} is {@code null} or size is 0
     */
    public List<String> keysWithPrefix(String prefix) {
        if (prefix == null || prefix.length() < 1) {
            throw new IllegalArgumentException("argument to keysWithPrefix() is null or size is 0");
        }
        TrieNode trieNode = getEndNode(prefix);
        if (trieNode == null) {
            return null;
        }
        ArrayList<String> keys = new ArrayList<>();
        for (Character ch : trieNode.next.keySet()) {
            collectHelper(prefix + ch, trieNode.next.get(ch), keys);
        }
        return keys;
    }

    /**
     * Returns all ids in the trie set that match the exact key.
     *
     * @param key the search string
     * @return the list of ids that match the exact key
     * @throws IllegalArgumentException if {@code key} is {@code null} or size is 0
     */
    public List<Node> idsMatched(String key) {
        if (key == null || key.length() < 1) {
            throw new IllegalArgumentException("argument to keysWithPrefix() is null or size is 0");
        }
        TrieNode trieNode = getEndNode(cleanString(key));
        if (trieNode == null) {
            return null;
        }
        return trieNode.nodes;
    }

    /**
     * Helper function to get the last node of the given {@code key}.
     *
     * @param key the key
     * @return the last node of the given {@code key}.
     */
    private TrieNode getEndNode(String key) {
        TrieNode curr = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (!curr.next.containsKey(ch)) {
                return null;
            }
            curr = curr.next.get(ch);
        }
        return curr;
    }

    /**
     * Helper function to recursively add the keys with the given prefix, and
     * moves on the the following characters in the trie table.
     *
     * @param s the prefix string
     * @param n the node
     * @param keys the list of keys with the given prefix
     */
    private void collectHelper(String s, TrieNode n, List<String> keys) {
        if (n.isKey == true) {
            keys.add(n.fullkey);
        }
        for (Character ch : n.next.keySet()) {
            collectHelper(s + ch, n.next.get(ch), keys);
        }
    }

    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
