package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * Represents a collection of non-overlapping (disjoint) sets.
 * Two sets are said to be disjoint sets if they have no element in common.
 * <p>
 * (A) (B) Where And B are both disjoint sets.
 * (A B) (B C) are NOT disjoint sets because they share element B.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    private int[] pointers;
    private IDictionary<T, Integer> items;
    private int numElements;

    /**
     * Constructs an empty ArrayDisjointSet of fixed size 1024.
     */
    public ArrayDisjointSet() {
        this(1024);
    }

    /**
     * Constructs an empty ArrayDisjointSet of a given size.
     *
     * @param size The size to to initialize the starting array.
     */
    public ArrayDisjointSet(int size) {
        pointers = new int[size];
        items = new ChainedHashDictionary<>();
        numElements = 0;
    }

    /**
     * Creates a new set containing just the given item.
     * The item is internally assigned an integer id.
     *
     * @param item The content to add to a NEW set.
     * @throws IllegalArgumentException if the item is already a part of this disjoint set somewhere
     */
    @Override
    public void makeSet(T item) {
        int index = getIndex(item);
        if (index == -1) {
            if (numElements == pointers.length) {
                resize(numElements);
            }
            pointers[numElements] = -1;
            items.put(item, numElements++);
        } else {
            throw new IllegalArgumentException("ERROR: The item was already added to the set!");
        }
    }

    /**
     * Returns the integer id of the set associated with the given item.
     *
     * @param item The desired item of which the retrieved id is associated.
     * @throws IllegalArgumentException if the item is not contained inside this disjoint set
     */
    @Override
    public int findSet(T item) {
        int index = getIndex(item);
        if (index != -1) {
            return findSet(index);
        }
        throw new IllegalArgumentException("ERROR: The item does not exist.");
    }


    /**
     * Recursive helper nature of findSet.
     *
     * @return The index of containing set.
     */
    private int findSet(int index) {
        if (pointers[index] < 0) {
            return index;
        } else {
            pointers[index] = findSet(pointers[index]);
            return pointers[index];
        }
    }


    /**
     * Finds the two sets associated with the given items, and combines the two sets together.
     *
     * @throws IllegalArgumentException if either item1 or item2 is not contained inside this disjoint set.
     * @throws IllegalArgumentException if item1 and item2 are already a part of the same set.
     */
    @Override
    public void union(T item1, T item2) {
        int root1 = findSet(item1);
        int root2 = findSet(item2);

        if (root1 == root2) {
            throw new IllegalArgumentException("ERROR: The two element are of the same set.");
        }
        unionByRoot(root1, root2);
    }

    /**
     * Retrieves the index at which an item is located.
     *
     * @param item The item to locate.
     * @return The associated int location of the item.
     */
    private int getIndex(T item) {
        if (items.containsKey(item)) {
            return items.get(item);
        }
        return -1;
    }

    /**
     * Unions two sets based on id.
     * givens sets [A, B, C, D]   -> call unionByRoot(0,3)
     * sets results: [A D, B, C]
     *
     * @param root1 location of set 1.
     * @param root2 location of set 2.
     */
    private void unionByRoot(int root1, int root2) {
        if (pointers[root2] < pointers[root1]) {
            pointers[root1] = root2;
        } else if (pointers[root1] == pointers[root2]) {
            pointers[root2] = root1;
            pointers[root1]--;
        } else {
            pointers[root2] = root1;
        }
    }

    /**
     * Given the pointer array is full, it is doubled in size.
     * The original content is preserved in the new array of size 2n.
     */
    private void resize(int size) {
        int[] tempPointers = new int[size * 2];

        for (int i = 0; i < size; i++) {
            tempPointers[i] = pointers[i];
        }
        pointers = tempPointers;
    }
}
