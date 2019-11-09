package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Assert;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    private IPriorityQueue<Integer> createHeap(int start, int end, int step) {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = start; i < end; i += step) {
            heap.insert(i);
        }
        return heap;
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        Assert.assertTrue(!heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testForSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap = createHeap(0, 300, 1);
        assertEquals(300, heap.size());
        Assert.assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testEmptyHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertEquals(0, heap.size());
        Assert.assertTrue(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testCorrectOrder() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap = createHeap(0, 100, 1);
        for (int i = 0; i < 100; i++) {
            assertEquals(100 - i, heap.size());
            assertEquals(i, heap.removeMin());
        }
        Assert.assertTrue(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testForRemoveMinException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap = createHeap(0, 10, 1);
        int min;
        for (int i = 0; i < 11; i++) {
            try {
                min = heap.removeMin();
                assertEquals(i, min);
            } catch (EmptyContainerException e) {
                Assert.assertTrue(i == 10);
            }
        }
    }

    @Test(timeout=SECOND)
    public void testRemoveLikeValues() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap = createHeap(0, 100, 1);
        for (int i = 0; i < 100; i++) {
            heap.insert(i);
        }
        int min;
        for (int i = 0; i < 200; i++) {
            min = heap.removeMin();
            assertEquals(i / 2, min);
        }
        Assert.assertTrue(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap = createHeap(0, 100, 1);
        int min;
        for (int i = 0; i < 100; i++) {
            min = heap.peekMin();
            assertEquals(heap.peekMin(), min);
            assertEquals(min, i);
            assertEquals(min, heap.removeMin());
            if (i != 99 && min == heap.peekMin()) {
                Assert.fail();
            }
        }
        Assert.assertTrue(heap.isEmpty());
        try {
            heap.peekMin();
            Assert.fail();
        } catch (EmptyContainerException e) {
            //DO nothing
        }
    }

    @Test(timeout=SECOND)
    public void testInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Assert.assertTrue(heap.isEmpty());
        for (int i = 99; i >= 0; i--) {
            assertEquals(99 - i, heap.size());
            heap.insert(i);
            assertEquals(i, heap.peekMin());
            assertEquals(100 - i, heap.size());
        }
    }

    @Test(timeout=SECOND)
    public void testForNullException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap = createHeap(0, 10, 1);
        try {
            heap.insert(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertNotEquals(e, null);
        }
    }
}