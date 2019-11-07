package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;

    // You're encouraged to add extra fields (and helper methods) though!

    //The basic integer data that holds the actual used size of the array (not the length which could have unused space)
    private int size;

    /**
     * The basic constructor for the arrayDictionary
     * Creates an array of standard size of 1024
     * Used size (integer size) is 0 b/c upon creation it is empty
     */
    public ArrayDictionary() {
        pairs = makeArrayOfPairs(8);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     * <p>
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    /**
     * The basic method that searches the Array for the pair with the given key value and returns the matching value
     *
     * @param key - the value for the pair that we are searching for
     * @return - the matching value for the passed key
     * Throws NoSuchKeyException - If the key is not in the array
     */
    @Override
    public V get(K key) {
        for (int i = 0; i < size(); i++) {
            if (isEqualKey(i, key)) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException("Key cannot be found");
    }

    /**
     * The basic method used to add a key/value pair into the array or update the value if the key already exists
     *
     * @param key   - the key that we are adding/updating
     * @param value - the value that we are adding/updating
     */
    @Override
    public void put(K key, V value) {
        for (int i = 0; i < size(); i++) {
            if (isEqualKey(i, key)) {
                pairs[i].value = value;
                return;
            }
        }
        if (size() == pairs.length) {
            resize();
        }
        pairs[size()] = new Pair<K, V>(key, value);
        size++;
    }

    /**
     * The method that searches for a given key and removes and returns the pair from the array
     *
     * @param key - the key we are searching for
     * @return - the value that will be returned once it is removed
     * Throws NoSuchKeyException - if the key/value pair doesn't exist
     */
    @Override
    public V remove(K key) {
        int i;
        V returnValue = null;
        //Find the key pair
        for (i = 0; i < size(); i++) {
            if (isEqualKey(i, key)) {
                returnValue = pairs[i].value;
                break;
            }
        }
        if (i == size()) {
            throw new NoSuchKeyException("The Key does not exist");
        }
        //Shift all of the array elements down
        for (int j = i; j < size() - 1; j++) {
            pairs[j] = pairs[j + 1];
            pairs[j] = new Pair<>(pairs[j + 1].key, pairs[j + 1].value);
        }
        pairs[size() - 1] = null;
        size--;
        return returnValue;
    }

    /**
     * Searches thru the array and returns true if the key exists
     *
     * @param key - the key we are searching for
     * @return - true if the key exists, false otherwise
     */
    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < size(); i++) {
            if (isEqualKey(i, key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the active size of the array (not the actual length which might include unused space)
     *
     * @return size
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {

        Iterator<KVPair<K, V>> iter = new Iterator<KVPair<K, V>>() {
            private int index = 0;

            /**
             * Returns wheter or not the iterator has another element.
             */
            @Override
            public boolean hasNext() {
                return index < size();
            }

            /**
             * Returns the next element of the iterator.
             */
            @Override
            public KVPair<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Error");
                }
                return new KVPair<K, V>(pairs[index].key, pairs[index++].value);
            }
        };
        return iter;
    }

    //Helper Methods!

    /**
     * Resize Method that is used whenever another element is being added into a full array
     * Doubles the size of the array
     */
    private void resize() {
        Pair<K, V>[] temp = makeArrayOfPairs(pairs.length * 2);
        for (int i = 0; i < pairs.length; i++) {
            temp[i] = pairs[i];
        }
        pairs = temp;
    }

    /**
     * A helper method that is a quick check for if the key matches
     *
     * @param i   - Where in the array we are comparing
     * @param key - What we are comparing the key value to
     * @return - true if
     * 1) The Key is null and the key at i is null (uses '==' comparison for null value)
     * 2) The Key matches the key at i (uses .equals comparison for objects)
     */
    private boolean isEqualKey(int i, K key) {
        return (key == null || pairs[i].key == null) ? key == null && pairs[i].key == null : pairs[i].key.equals(key);
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns a string of the key value pair.
         * */
        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}