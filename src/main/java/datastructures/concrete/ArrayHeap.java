package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int DEFAULT_CAPACITY = 1024;
    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int curSize;

    // Feel free to add more fields and constants.

    /**
     * Initializes the ArrayHeap by creating an empty generic array and setting the size to 0
     */
    public ArrayHeap() {
        heap = makeArrayOfT(DEFAULT_CAPACITY);
        curSize = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    /**
     * Method that finds and removes the minimum value from the heap
     * @return - the minimum value
     */
    @Override
    public T removeMin() {
        //Save the minimum value
        T min = peekMin();
        //Set the last element to the top spot and percolate down in order to get the new minimum value at the top
        heap[0] = heap[--curSize];
        percolateDown(0);
        return min;
    }

    /**
     * Method that returns the minimum value from the heap without removing the element
     * @return - the minimum value
     * Throws - EmptyContainerException - if the heap is empty
     */
    @Override
    public T peekMin() {
        if (size() == 0) {
            throw new EmptyContainerException("Heap has no work");
        }
        return heap[0];
    }

    /**
     * Method that takes the given parameter and inserts the element into the heap
     * Resizes the internal array if it is full when the insert is called
     * @param item - the item to insert into the heap
     * Throws - IllegalArgumentException - if the item passed into the method is null
     */
    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null!");
        }
        if (size() == heap.length - 1) {
            enlargeArray(heap.length * 2 + 1);
        }
        int hole = curSize++;
        heap[hole] = item;
        percolateUp(hole);
    }

    /**
     * Method that returns the number of elements in the heap.
     * @return - curSize (number of elements)
     */
    @Override
    public int size() {
        return curSize;
    }

    /**
     * Helper method that is used to resize the internal array
     * @param newCapacity - the size to resize the array to
     */
    private void enlargeArray(int newCapacity) {
        T[] oldData = heap;
        heap = makeArrayOfT(newCapacity);
        for (int i = 0; i < oldData.length; i++) {
            heap[i] = oldData[i];
        }
    }

    /**
     * Helper method that is used to sort elements in the heap
     * @param hole - the spot at which to start the percolation
     */
    private void percolateUp(int hole) {
        int parentIndex = (hole - 1) / 4;
        T work = heap[hole];
        while (hole > 0 && work.compareTo(heap[parentIndex]) < 0) {
            heap[hole] = heap[parentIndex];
            hole = parentIndex;
            parentIndex = (hole - 1) / 4;
        }
        heap[hole] = work;
    }

    /**
     * Helper method that is used to sort elements in the heap
     * @param hole - the spot at which to start the percolation
     */
    private void percolateDown(int hole) {
        int childIndex1 = hole * 4 + 1;

        T data = heap[hole];
        //If I have at least one child and if that child is smaller than data
        while (isValidChild(childIndex1, data)) {
            int smallerIndex = getSmallerIndex(childIndex1);
            heap[hole] = heap[smallerIndex];
            hole = smallerIndex;
            childIndex1 = hole * 4 + 1;
        }
        heap[hole] = data;
    }

    public void remove(T item){
        throw new  UnsupportedOperationException();
    }
    /**
     * Helper method that is used to check if the child is a valid child
     * @param startIndex - the index of the first child
     * @param data - the data to check against
     * @return - the boolean expression for whether this is a valid child
     */
    private boolean isValidChild(int startIndex, T data) {
        for (int i = startIndex; i <= startIndex + 3; i++) {
            if (i < curSize && data.compareTo(heap[i]) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method that is used to find the index of the smallest child
     * @param startIndex - the index of the first child
     * @return - the index of the child with the smallest data
     */
    private int getSmallerIndex(int startIndex) {
        int smallerIndex = startIndex;
        for (int i = startIndex + 1; i <= startIndex + 3; i++) {
            if (i < curSize && heap[i].compareTo(heap[smallerIndex]) < 0) {
                smallerIndex = i;
            }
        }
        return smallerIndex;
    }


}