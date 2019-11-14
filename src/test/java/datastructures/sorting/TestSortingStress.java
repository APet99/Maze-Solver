package datastructures.sorting;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.Searcher;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test(timeout=10*SECOND)
    public void testArrayHeap() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = 0; i < 420024; i++) {
            heap.insert(i);
        }
        assertEquals(420024, heap.size());
        for (int i = 0; i < 420024; i++) {
            assertEquals(i, heap.removeMin());
        }
        assertTrue(heap.isEmpty());
    }

    @Test(timeout=10*SECOND)
    public void testArrayHeapVeryLarge() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = 0; i < 999999; i++) {
            heap.insert(i);
        }
        assertEquals(999999, heap.size());
        for (int i = 0; i < 999999; i++) {
            assertEquals(i, heap.removeMin());
        }
        assertTrue(heap.isEmpty());
    }

    @Test(timeout=10*SECOND)
    public void testTopKSort() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 420023; i >= 0; i--) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(420000, list);
        assertEquals(420000, top.size());
        int count = 24;
        for (Integer val : top) {
            assertEquals(val, count++);
        }
    }

    @Test(timeout=10*SECOND)
    public void testTopKSortVeryLarge() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 999999; i >= 0; i--) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(999900, list);
        assertEquals(999900, top.size());
        int count = 100;
        for (Integer val : top) {
            assertEquals(val, count++);
        }
    }
}