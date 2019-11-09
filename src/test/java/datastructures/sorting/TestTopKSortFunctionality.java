package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;
import org.junit.Assert;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {

    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testWithNullInput() {
        IList<Integer> top = Searcher.topKSort(5, null);
        Assert.assertNull(top);
    }

    @Test(timeout=SECOND)
    public void testWithZeroK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(0, list);

        assertEquals(0, top.size());
    }

    @Test(timeout=SECOND)
    public void testWithListOfSizeZero() {
        IList<Integer> list = new DoubleLinkedList<>();

        IList<Integer> top = Searcher.topKSort(5, list);

        assertEquals(0, top.size());
    }

    @Test(timeout=SECOND)
    public void testWithNegativeK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        try {
            IList<Integer> top = Searcher.topKSort(-1, list);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test(timeout=SECOND)
    public void testWithKOf1() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(1, list);

        assertEquals(1, top.size());
        assertEquals(19, top.get(0));
    }

    @Test(timeout=SECOND)
    public void testWithKGreaterThanInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(10, list);

        assertEquals(5, top.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testWithLargeListSmallK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 500; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);

        assertEquals(5, top.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(495 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testLargeListLargeK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 420; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(400, list);

        assertEquals(400, top.size());
        for (int i = 0; i < 400; i++) {
            assertEquals(20 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testListDoesNotChange() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 420; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(400, list);

        assertEquals(400, top.size());
        for (int i = 0; i < 400; i++) {
            assertEquals(20 + i, top.get(i));
        }
        for (int i = 0; i < 420; i++) {
            assertEquals(i, list.get(i));
        }
    }
}
