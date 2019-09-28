import java.util.Iterator;
import java.util.Set;

/**             TreeMap
 * This class is a BST-based implementation of the Map61B interface, which
 * represents a basic tree-based map.
 * @author Hsingyi Lin
 * date    09/28/2019
 */

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {

    private K key = null;
    private V value = null;
    private BSTMap left = null;
    private BSTMap right = null;
    private BSTMap parent = null;
    private int size;

    public BSTMap(K k, V v) {
        key = k;
        value = v;
        size = 1;
    }

    public BSTMap() {
        size = 0;
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    public int size() { return size; };

    /** Removes all of the mappings from this map. */
    public void clear() {
        key = null;
        value = null;
        left = null;
        right = null;
        size = 0;
    };

    /**
     * Returns true if this map contains a mapping for the specified key.
     */
    public boolean containsKey(K k) {
        if (key == null) {
            return false;
        }
        return find(this, k) != null;
    };

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K k) {
        BSTMap T = find(this, k);
        if (T == null) {
            return null;
        }
        return (V) T.value;
    };

    private BSTMap find(BSTMap T, K k) {
        if (T == null || T.key == null || k == null) {
            return null;
        }
        if (k.equals(T.key)) {
            return T;
        } else if (k.compareTo(T.key) < 0) {
            return find(T.left, k);
        } else {
            return find(T.right, k);
        }
    }

    /**
     * Associates the specified value with the specified key in this map.
     */
    public void put(K k, V v) {
        if (find(this, k) != null || k == null) {
            return;
        }
        if (key == null) {
            key = k;
            value = v;
        } else if (k.compareTo(key) < 0) {
            left = insert(this, left, k, v);
        }
        else {
            right = insert(this, right, k, v);
        }
        size++;
    };

    private BSTMap insert(BSTMap parent, BSTMap T, K k, V v) {
        if (T == null) {
            BSTMap newNode = new BSTMap(k, v);
            newNode.parent = parent;
            return newNode;
        }
        if (k.compareTo(T.key) < 0) {
            T.left = insert(T, T.left, k, v);
        } else if (k.compareTo(T.key) > 0) {
            T.right = insert(T, T.right, k ,v);
        }
        T.size++;
        return T;
    }

    /**
     * Prints out the BSTMap in order of increasing Key.
     */
    public void printInOrder() {
        printHelper(this);
        System.out.println();
        if (left != null) {
            System.out.println("left size: " + left.size());
        }
        if (right != null) {
            System.out.println("right size: " + right.size());
        }
    }

    private void printHelper(BSTMap T) {
        if (T == null) {
            return;
        }
        printHelper(T.left);
        System.out.print(T.key + "\t");
        printHelper(T.right);
    }

    /**
     * Returns the given node's child node. If it has two child,
     * nodes, return the left node.
     */
    private BSTMap findChild(BSTMap node) {
        if (node == null) {
            return null;
        }
        if (node.left != null) {
            return node.left;
        }
        else if (node.right != null) {
            return node.right;
        }
        return null;
    }

    /**
     * Returns the given tree's leftmost or rightmost node.
     * Assume that the given tree has two child nodes.
     */
    private BSTMap replacement(BSTMap node) {
        if (node == null || node.right == null || node.left == null) {
            return null;
        }
        if (node.right.size() > node.left.size()) {
            BSTMap p = node.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        } else {
            BSTMap p = node.left;
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }
    }

    /**
     * If this map contains the given key, remove the key node from the tree
     * and returns the key value, otherwise returns null.
     */
    @Override
    public V remove(K key) {
        BSTMap node = find(this, key);
        // key is not found
        if (node == null) {
            return null;
        }
        V val = (V) node.value;
        // node of the given key contains no child node
        if (node.left == null && node.right == null) {
            // node is the root of the tree
            if (node.parent == null) {
                node = null;
                size--;
            }
            unconnect(node);
        }
        // node of the given key contains two child nodes
        else if (node.left != null && node.right != null) {
            BSTMap r = replacement(node);
            unconnect(r);
            node.key = r.key;
            node.value = r.value;
        }
        // node of the given key contains one child node
        else {
            // node is the root of the tree
            if (node.parent == null) {
                node = findChild(node);
                node.parent = null;
                size--;
            }
            unconnect(node);
        }
        return val;
    };

    /**
     * Connects the given node's parent and child, thus leaves
     * the given node unconnected. Works only when the given node
     * has at most one child.
     */
    private void unconnect(BSTMap node) {
        if (node == null) {
            return;
        }
        // update size of related nodes
        updateParentSize(node.parent, -1);
        updateChildSize(node, -1);
        // update connections
        BSTMap c = findChild(node);
        if (node.parent != null) {
            if (node.key.compareTo(node.parent.key) < 0) {
                node.parent.left = c;
            } else {
                node.parent.right = c;
            }
        }
        if (c != null) {
            c.parent = node.parent;
        }
    }

    private void updateParentSize(BSTMap T, int x) {
        if (T == null) {
            return;
        }
        updateParentSize(T.parent, x);
        T.size += x;
    }

    private void updateChildSize(BSTMap T, int x) {
        if (T == null) {
            return;
        }
        updateChildSize(T.left, x);
        updateChildSize(T.right, x);
        T.size += x;
    }

    @Override
    public V remove(K key, V value) {
        if (get(key) != value) {
            return null;
        }
        return remove(key);
    };

    @Override
    public Set<K> keySet() { throw new UnsupportedOperationException(); };

    @Override
    public Iterator iterator() { throw new UnsupportedOperationException(); }
}
