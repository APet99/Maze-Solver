package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    private int[] pointers;
    private IDictionary<T, Integer> items;
    private int numElements;

    //can add fields and helper methods

    public ArrayDisjointSet() {
        this(1024);
    }

    public ArrayDisjointSet(int size) {
        pointers = new int[size];
        items = new ChainedHashDictionary<>();
        numElements = 0;
    }

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

    @Override
    public int findSet(T item) {
        int index = getIndex(item);
        if (index != -1) {
            return findSet(index);
        }
        throw new IllegalArgumentException("ERROR: The item does not exist.");
    }

    private int findSet(int index) {
        if (pointers[index] < 0) {
            return index;
        } else {
            pointers[index] = findSet(pointers[index]);
            return pointers[index];
        }
    }

    @Override
    public void union(T item1, T item2) {
        int root1 = findSet(item1);
        int root2 = findSet(item2);

        if (root1 == root2) {
            throw new IllegalArgumentException("ERROR: The two element are of the same set.");
        }
        unionByRoot(root1, root2);
    }

    private int getIndex(T item) {
        if (items.containsKey(item)) {
            return items.get(item);
        }
        return -1;
    }

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

    private void resize(int size) {
        int[] tempPointers = new int[size * 2];

        for (int i = 0; i < size; i++) {
            tempPointers[i] = pointers[i];
        }
        pointers = tempPointers;
    }
}
