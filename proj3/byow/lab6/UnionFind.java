package byow.lab6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**             Union Find (Disjoint Sets)
 * This program uses Weighted Quick Union (WQU) with / without
 * path compression implementation.
 * @Author Hsingyi Lin
 * date    09/26/2019
 */

public class UnionFind<Item> {

    private static final double LOADFACTOR = 0.75;

    /**
     * Array[i] is the parent item of item i. If item i is the root,
     * Array[i] is the negative size of the set i belongs to.
     */
    private int[] parentArray;
    private HashMap<Integer, Item> items;
    private int size;


    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int initialSize) {
        parentArray = new int[initialSize];
        this.items = new HashMap<>();
        for (int i = 0; i < initialSize; i++) {
            parentArray[i] = -1;
        }
        this.size = 0;
    }

    public int add(Item item) {
        if (size / parentArray.length > LOADFACTOR) {
            resize(parentArray.length * 2);
        }
        items.put(size, item);
        size++;
        return size - 1;
    }

    private void resize(int newSize) {
        int[] newParentArray = new int[newSize];
        for (int i = 0; i < parentArray.length; i++) {
            newParentArray[i] = parentArray[i];
        }
        for (int i = parentArray.length; i < newSize; i++) {
            newParentArray[i] = -1;
        }
        parentArray = newParentArray;
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        if (vertex < 0 || vertex >= parentArray.length) {
            throw new IllegalArgumentException("Invalid input vertex " + vertex);
        }
    }

    /* Returns the size of the set v1 belongs to. */
    private int sizeOf(int v1) {
        return -parentArray[find(v1)];
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    private int parent(int v1) {
        validate(v1);
        return parentArray[v1];
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    private int find(int vertex) {
        while (parent(vertex) > -1) {
            vertex = parent(vertex);
        }
        return vertex;
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid 
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a 
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        int root1 = find(v1);
        int root2 = find(v2);
        if (root1 == root2) {
            return;
        }
        if (sizeOf(root1) > sizeOf(root2)) {
            parentArray[root1] += parentArray[root2];
            parentArray[root2] = root1;
        }
        else {
            parentArray[root2] += parentArray[root1];
            parentArray[root1] = root2;
        }
    }

    public void unionWithPathCompassion(int v1, int v2) {
        int root1 = find(v1);
        int root2 = find(v2);
        int newRoot = root2;
        int newSize = sizeOf(root2);
        if (root1 != root2) {
            newSize += sizeOf(root1);
        }
        if (sizeOf(root1) > sizeOf(root2)) {
            newRoot = root1;
        }
        parentArray[root1] = newRoot;
        parentArray[root2] = newRoot;
        parentArray[newRoot] = -newSize;

        updateParent(v1, newRoot);
        updateParent(v2, newRoot);
    }

    private void updateParent(int v, int p) {
        while (parent(v) > -1) {
            int tempV = parent(v);
            parentArray[v] = p;
            v = tempV;
        }
    }

    public int size() {
        return size;
    }

    public boolean allConnected() {
        System.out.println(maxConnected() + " " + size);
        return -parentArray[find(0)] == size;
    }

    public int maxConnected() {
        int max = 0;
        for (int i = 0; i < size; i++) {
            if (-parentArray[i] > max) {
                max = -parentArray[i];
            }
        }
        return max;
    }

    public int connectedItems(int index) {
        return -parentArray[find(index)];
    }

    public Item getItem(int index) {
        return items.get(index);
    }

    public int getIndex(Item item) {
        for (int i : items.keySet()) {
            if (item.equals(items.get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < parentArray.length; i++) {
            output.append(parentArray[i] + "\t");
        }
        return output.toString();
    }

}
