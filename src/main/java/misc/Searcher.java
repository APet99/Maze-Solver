package misc;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

public class Searcher {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "biggest".
     *
     * If the input list contains fewer then 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        // Implementation notes:
        //
        // - This static method is a _generic method_. A generic method is similar to
        //   the generic methods we covered in class, except that the generic parameter
        //   is used only within this method.
        //
        //   You can implement a generic method in basically the same way you implement
        //   generic classes: just use the 'T' generic type as if it were a regular type.
        //
        // - You should implement this method by using your ArrayHeap for the sake of
        //   efficiency.

        if (k < 0) {
            throw new IllegalArgumentException("k cannot be less than 0");
        }
        if (input == null) {
            return null;
        }
        //If k is greater than the size of the input list, then reset k to be input size
        if (input.size() < k) {
            k = input.size();
        }
        if (k == 0) {
            return new DoubleLinkedList<>();
        }
        IPriorityQueue<T> heap = new ArrayHeap<>();
        int counter = 0;
        for (T cur : input) {
            if (counter < k) {
                heap.insert(cur);
                counter++;
            } else {
                if (heap.peekMin().compareTo(cur) < 0) {
                    heap.removeMin();
                    heap.insert(cur);
                }
            }
        }
        IList<T> result = new DoubleLinkedList<>();
        for (int i = 0; i < k; i++) {
            result.insert(i, heap.removeMin());
        }
        return result;
    }
}