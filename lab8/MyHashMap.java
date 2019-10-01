import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * Implementation of HashMap.
 * @author Hsingyi Lin
 * date    10/01/2019
 */

public class MyHashMap<K  extends Comparable, V> implements Map61B<K, V> {
    private static final int INIT_SIZE = 16;
    private static final double INIT_LOADFACTOR = 0.75;

    private int n;                         // number of pairs in the hash table
    private int m;                         // number of buckets
    private LinkedList<Pair>[] map;        // array of lists of key-value pairs
    private double loadFactor;             // load factor for measuring n / m
    private HashSet<K> h;                  // HashSet of keys

    /**
     * Represents one key-value pair.
     */
    private class Pair {

        K key;
        V value;

        Pair(K k, V v) {
            key = k;
            value = v;
        }

        Pair get(K k) {
            if (key == k) {
                return this;
            }
            return null;
        }

    }

    /**
     * Initializes an empty hash table.
     */
    public MyHashMap() {
        this(INIT_SIZE, INIT_LOADFACTOR);
    }

    /**
     * Initializes an empty hash table with {@code initialSize} buckets.
     *
     * @param initialSize the initial number of buckets
     */
    public MyHashMap(int initialSize) {
        this(initialSize, INIT_LOADFACTOR);
    }

    /**
     * Initializes an empty hash table with {@code initialSize} buckets
     * and loadFactor {@code loadFactor}.
     *
     * @param initialSize the initial number of buckets
     * @param loadFactor loadFactor
     */
    public MyHashMap(int initialSize, double loadFactor) {
        n = 0;
        m = initialSize;
        map = new LinkedList[m];
        for (int i = 0; i < m; i++) {
            map[i] = new LinkedList<>();
        }
        this.loadFactor = loadFactor;
        h = new HashSet();
    }

    /**
     * Returns the number of key-value mappings in this hash table.
     *
     * @return the number of key-value mappings in this hash table
     */
    public int size() { return n; };

    /**
     * Removes all of the mappings from this hash table.
     */
    public void clear() {
        for (int i = 0; i < m; i++) {
            map[i].clear();
        }
        n = 0;
    };

    /**
     * Returns true if this hash table contains the specified key.
     *
     * @param k the key
     * @return {@code true} if this hash table contains {@code k};
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code k} is {@code null}
     */
    public boolean containsKey(K k) {
        if (k == null) {
            throw new IllegalArgumentException("argument to containsKey() is null");
        }
        return getPair(k) != null;
    };

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param k the key
     * @return the value associated with {@code k} in the hash table;
     *         {@code null} if no such value
     * @throws IllegalArgumentException if {@code k} is {@code null}
     */
    public V get(K k) {
        if (k == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        if (getPair(k) != null) {
            return getPair(k).value;
        }
        return null;
    }

    // returns the pair with the given key
    private Pair getPair(K k) {
        for (int i = 0; i < map[hash(k)].size(); i++) {
            if (map[hash(k)].get(i).key.equals(k)) {
                return map[hash(k)].get(i);
            }
        }
        return null;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     *
     * @param k the key
     *        v the value
     * @throw IllegalArgumentException if {@code k} or {code v} is {@code null}
     */
    public void put(K k, V v) {
        if (k == null) {
            throw new IllegalArgumentException("argument to put() is null");
        }
        if (containsKey(k)) {
            getPair(k).value = v;
            return;
        }
        if (n / m > loadFactor) {
            resize(m * 2);
        }
        map[hash(k)].add(new Pair(k, v));
        n++;
        h.add(k);
    }

    private void resize(int newM) {
        LinkedList<Pair>[] newMap = new LinkedList[newM];
        for (int i = 0; i < newM; i++) {
            newMap[i] = new LinkedList<>();
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < map[i].size(); j++) {
                newMap[hash(map[i].get(j).key)].add(map[i].get(j));
            }
        }
        map = newMap;
        m = newM;
    }

    public void printInOrder() {
        for (int i = 0; i < m; i++) {
            System.out.println("bucket #" + i);
            for (int j = 0; j < map[i].size(); j++) {
                System.out.println("\t" + map[i].get(j).key + "\t" + map[i].get(j).value);
            }
        }
    }

    // hash value between 0 and m-1
    private int hash(K k) {
        return (k.hashCode() & 0x7fffffff) % m;
    }

    @Override
    public Set<K> keySet() {
        return h;
    }
    
    @Override
    public Iterator iterator() {
        return h.iterator();
    }

    /**
     * Removes the specified key and its associated value from this hash table
     * (if the key is in this hash table).
     *
     * @param k the key
     * @return the value associated with {@code k} in the hash table;
     *         {@code null} if no such key
     * @throw IllegalArgumentException if {@code k} is {@code null}
     */
    @Override
    public V remove(K k) {
        if (k == null) {
            throw new IllegalArgumentException("argument to remove() is null");
        }
        if (!containsKey(k)) {
            return null;
        }
        for (int i = 0; i < map[hash(k)].size(); i++) {
            if (map[hash(k)].get(i).key.equals(k)) {
                V val = map[hash(k)].get(i).value;
                map[hash(k)].remove(i);
                n--;
                return val;
            }
        }
        return null;
    }

    /**
     * Removes the specified key and its associated value from this hash table
     * (if the key-value pair is in this hash table).
     *
     * @param k the key
     *        v the value
     * @return the value associated with {@code k} in the hash table;
     *         {@code null} if no such key-value pair
     */
    @Override
    public V remove(K k, V v) {
        if (get(k) == v) {
            return remove(k);
        }
        return null;
    }

}
