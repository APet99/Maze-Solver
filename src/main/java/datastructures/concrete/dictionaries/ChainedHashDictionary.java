package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private final double loadFactor = 0.70; //This should be a LOAD_FACTOR
    private static final int[] PRIMES;

    static {
        PRIMES = new int[] {313, 751, 1553, 3109, 6229, 12491, 24989, 49993, 99991, 199999, 400009, 800029, 1600061,
                3200123, 6400123, 12800251, 25600511, 51201047};
    }

    private IDictionary<K, V>[] chains;
    private int arraySize;

    // constructor:
    public ChainedHashDictionary() {
        chains = makeArrayOfChains(1553);
        arraySize = 0;
    }

    /**
     * Constructor containing a size param
     * Used primarily when we know we are making a lot of relatively smaller dictionaries
     * @param size - the size to initialize the dictionary to
     */
    public ChainedHashDictionary(int size) {
        chains = makeArrayOfChains(size);
        arraySize = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     * <p>
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    /**
     * Returns the value corresponding to the given key.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V get(K key) {
        checkForChain(key);
        return chains[hasher(key, chains.length)].get(key);
    }

    /**
     * Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
     * replace its value with the given one.
     */
    @Override
    public void put(K key, V value) {
        if (chains[hasher(key, chains.length)] == null) {
            chains[hasher(key, chains.length)] = new ArrayDictionary<>();
        }
        if (!chains[hasher(key, chains.length)].containsKey(key)) {
            arraySize++;
            chains[hasher(key, chains.length)].put(key, value);
            checkForResize();
        } else {
            chains[hasher(key, chains.length)].put(key, value);
        }
    }

    /**
     * Remove the key-value pair corresponding to the given key from the dictionary.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V remove(K key) {
        checkForChain(key);
        V returnValue = chains[hasher(key, chains.length)].remove(key);
        arraySize--;
        return returnValue;
    }

    /**
     * Returns 'true' if the dictionary contains the given key and 'false' otherwise.
     */
    @Override
    public boolean containsKey(K key) {
        if (chains[hasher(key, chains.length)] == null) {
            return false;
        }
        return chains[hasher(key, chains.length)].containsKey(key);
    }

    /**
     * Returns the number of key-value pairs stored in this dictionary.
     */
    @Override
    public int size() {
        return arraySize;
    }


    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * if a chain is NUll (does not exist) NoSuchKeyException is thrown.
     */
    private void checkForChain(K key) {
        if (chains[hasher(key, chains.length)] == null) {
            throw new NoSuchKeyException("ERROR: The element is not there!");
        }
    }

    /**
     * Returns the new hash of a given key. Used when resizing.
     * if the hash is null, it is converted to a positive number by appending length to the hash.
     *
     * @return the new hash code(int).
     */
    private int hasher(K key, int length) {
        int hashCode;
        if (key == null) {
            hashCode = 0;
        } else {
            hashCode = key.hashCode() % length;
        }
        if (hashCode < 0) {
            hashCode += length;
        }
        return hashCode;
    }

    /**
     * checks if the load factor is exceeded. If it is, the chain is resized.
     * Otherwise nothing happens.
     */
    private void checkForResize() {
        if (1.0 * arraySize / chains.length >= loadFactor) {
            resize();
        }
    }

    /**
     * If the array needs to be resized, an array of 3/2 the original size is created.
     * All previous values are re-hashed and copiex to the new array.
     */
    private void resize() {
        IDictionary<K, V>[] temp = makeArrayOfChains((int) (getClosestPrime(chains.length * 2)));
        for (IDictionary<K, V> chain : chains) {
            if (chain != null) {
                for (KVPair<K, V> pair : chain) {
                    int hash = hasher(pair.getKey(), temp.length);
                    if (temp[hash] == null) {
                        temp[hash] = new ArrayDictionary<>();
                    }
                    temp[hash].put(pair.getKey(), pair.getValue());
                }
            }
        }
        chains = temp;
    }

    private int getClosestPrime(int closestTo) {
        for (int prime : PRIMES) {
            if (prime >= closestTo) {
                return prime;
            }
        }
        return PRIMES[PRIMES.length - 1];
    }

    /**
     * Hints:
     * <p>
     * 1. You should add extra fields to keep track of your iteration
     * state. You can add as many fields as you want. If it helps,
     * our reference implementation uses three (including the one we
     * gave you).
     * <p>
     * 2. Think about what exactly your *invariants* are. Once you've
     * decided, write them down in a comment somewhere to help you
     * remember.
     * <p>
     * 3. Before you try and write code, try designing an algorithm
     * using pencil and paper and run through a few examples by hand.
     * <p>
     * We STRONGLY recommend you spend some time doing this before
     * coding. Getting the invariants correct can be tricky, and
     * running through your proposed algorithm using pencil and
     * paper is a good way of helping you iron them out.
     * <p>
     * 4. Think about what exactly your *invariants* are. As a
     * reminder, an *invariant* is something that must *always* be
     * true once the constructor is done setting up the class AND
     * must *always* be true both before and after you call any
     * method in your class.
     * <p>
     * Once you've decided, write them down in a comment somewhere to
     * help you remember.
     * <p>
     * You may also find it useful to write a helper method that checks
     * your invariants and throws an exception if they're violated.
     * You can then call this helper method at the start and end of each
     * method if you're running into issues while debugging.
     * <p>
     * (Be sure to delete this method once your iterator is fully working.)
     * <p>
     * Implementation restrictions:
     * <p>
     * 1. You **MAY NOT** create any new data structures. Iterators
     * are meant to be lightweight and so should not be copying
     * the data contained in your dictionary to some other data
     * structure.
     * <p>
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     * instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int counter;
        private int index;
        private int size;
        private Iterator<KVPair<K, V>> itr;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            counter = 0;
            size = calculateSize();
            initializationHelper();
        }

        /**
         * Returns if the iterator has a next token to receive.
         */
        @Override
        public boolean hasNext() {
            return counter < size;
        }

        /**
         * advances the iterator one time.
         */
        @Override
        public KVPair<K, V> next() {
            if (itr == null) {
                throw new NoSuchElementException("Bruh it isn't there.");
            }
            if (!itr.hasNext()) {
                for (int i = index + 1; i < chains.length; i++) {
                    if (chains[i] != null && chains[i].size() != 0) {
                        index = i;
                        itr = chains[i].iterator();
                        break;
                    }
                }
            }
            counter++;
            return itr.next();
        }

        /**
         * Determines the size of the chain. Returns the value
         */
        private int calculateSize() {
            int count = 0;
            for (IDictionary<K, V> chain : chains) {
                if (chain != null) {
                    count += chain.size();
                }
            }
            return count;
        }

        /**
         * Helper method for the chainedIterator constructor.
         */
        private void initializationHelper() {
            for (int i = 0; i < chains.length; i++) {
                if (chains[i] != null && chains[i].size() != 0) {
                    index = i;
                    itr = chains[i].iterator();
                    return;
                }
            }
        }
    }
}