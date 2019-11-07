package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See ISet for more details on what each method is supposed to do.
 */
public class ChainedHashSet<T> implements ISet<T> {
    // This should be the only field you need
    private IDictionary<T, Boolean> map;

    //constructor:
    public ChainedHashSet() {
        // No need to change this method
        this.map = new ChainedHashDictionary<>();
    }

    /**
     * Constructor w/ the size parameter
     * Used primarily when we need a lot of small dictionaries
     * @param size - the size to initialize the set to
     */
    public ChainedHashSet(int size) {
        this.map = new ChainedHashDictionary<>(size);
    }

    /**
     * Adds the given item to the set.
     * <p>
     * If the item already exists in the set, this method does nothing.
     */
    @Override
    public void add(T item) {
        if (!map.containsKey(item)) {
            map.put(item, null);
        }
    }

    /**
     * Removes the given item from the set.
     *
     * @throws NoSuchElementException if the set does not contain the given item
     */
    @Override
    public void remove(T item) {
        if (map.size() > 0 && map.containsKey(item)) {
            map.remove(item);
        } else {
            throw new NoSuchElementException("ERROR: the item is not found. Can not remove.");
        }
    }

    /**
     * Returns 'true' if the set contains this item and false otherwise.
     */
    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    /**
     * Returns the number of items contained within this set.
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Returns all items contained within this set.
     */
    @Override
    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }

    private static class SetIterator<T> implements Iterator<T> {
        // This should be the only field you need
        private Iterator<KVPair<T, Boolean>> iter;

        /**
         * Sets the iterator
         */
        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            // No need to change this method.
            this.iter = iter;
        }

        /**
         * Returns if the iterator has another element
         */
        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        /**
         * Returns the next token of the iterator.
         */
        @Override
        public T next() {
            return iter.next().getKey();
        }
    }
}