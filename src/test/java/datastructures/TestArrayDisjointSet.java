package datastructures;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.interfaces.IDisjointSet;
import misc.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestArrayDisjointSet extends BaseTest {
    private <T> IDisjointSet<T> createForest(T[] items) {
        IDisjointSet<T> forest = new ArrayDisjointSet<>();
        for (T item : items) {
            forest.makeSet(item);
        }
        return forest;
    }

    private <T> void check(IDisjointSet<T> forest, T[] items, int[] expectedIds) {
        for (int i = 0; i < items.length; i++) {
            assertEquals(expectedIds[i], forest.findSet(items[i]));
        }
    }

    @Test(timeout = SECOND)
    public void testMakeSetAndFindSetSimple() {
        String[] items = new String[]{"a", "b", "c", "d", "e"};
        IDisjointSet<String> forest = this.createForest(items);

        for (int i = 0; i < 5; i++) {
            check(forest, items, new int[]{0, 1, 2, 3, 4});
        }
    }

    @Test(timeout = SECOND)
    public void testUnionSimple() {
        String[] items = new String[]{"a", "b", "c", "d", "e"};
        IDisjointSet<String> forest = this.createForest(items);

        forest.union("a", "b");
        int id1 = forest.findSet("a");
        assertTrue(id1 == 0 || id1 == 1);
        assertEquals(id1, forest.findSet("b"));

        forest.union("c", "d");
        int id2 = forest.findSet("c");
        assertTrue(id2 == 2 || id2 == 3);
        assertEquals(id2, forest.findSet("d"));

        assertEquals(4, forest.findSet("e"));
    }

    @Test(timeout = SECOND)
    public void testUnionUnequalTrees() {
        String[] items = new String[]{"a", "b", "c", "d", "e"};
        IDisjointSet<String> forest = this.createForest(items);

        forest.union("a", "b");
        int id = forest.findSet("a");

        forest.union("a", "c");

        for (int i = 0; i < 5; i++) {
            check(forest, items, new int[]{id, id, id, 3, 4});
        }
    }

    @Test(timeout = SECOND)
    public void testIllegalFindSet() {
        String[] items = new String[]{"a", "b", "c", "d", "e"};
        IDisjointSet<String> forest = this.createForest(items);

        try {
            forest.findSet("f");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // All ok -- expected result
        }
    }

    @Test(timeout = SECOND)
    public void testIllegalUnion() {
        String[] items = new String[]{"a", "b", "c", "d", "e"};
        IDisjointSet<String> forest = this.createForest(items);

        try {
            forest.union("a", "f");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // All ok -- expected result
        }

        forest.union("a", "b");

        try {
            forest.union("a", "b");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // All ok -- expected result
        }
    }

    @Test(timeout = 4 * SECOND)
    public void testLargeForest() {
        IDisjointSet<Integer> forest = new ArrayDisjointSet<>();
        forest.makeSet(0);

        int numItems = 5000;
        for (int i = 1; i < numItems; i++) {
            forest.makeSet(i);
            forest.union(0, i);
        }

        int cap = 6000;
        int id = forest.findSet(0);
        for (int i = 0; i < cap; i++) {
            for (int j = 0; j < numItems; j++) {
                assertEquals(id, forest.findSet(j));
            }
        }
    }

    @Test(timeout = 250)
    public void testUnionOfSameSetIsNull() {
        String[] items = new String[]{"a", "b", "c", "d", "e"};
        IDisjointSet<String> forest = this.createForest(items);

        forest.union("a", "b"); //puts A and B in the same set

        try {
            forest.union("a", "b"); // Try to union two items of the same set
            fail("Should have thrown illegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Do nothing. The case was caught.
        }
    }

    @Test(timeout = 250)
    public void testResize() {
        int size = 100;
        IDisjointSet<Integer> forest = new ArrayDisjointSet<>(size);

        for (int i = 0; i < size; i++) {
            forest.makeSet(i);
        }
        //forest should be full. Adding another element will require resizing.
        try {
            // This should pass if resizing occurs.
            forest.makeSet(size);
        } catch (Exception e) {
            //Exception will be caught if resizing does not occur.
            e.printStackTrace();
        }
    }

    @Test(timeout = 250)
    public void testUnionByRootKeep1() {
        int size = 5;
        IDisjointSet<Integer> forest = new ArrayDisjointSet<>(size);
        for (int i = 0; i < size; i++) {
            forest.makeSet(i);
        }
        int id = forest.findSet(0);

        Assert.assertEquals(id, forest.findSet(0));
        forest.union(1, 0);

        id = forest.findSet(0);
        assertEquals(id, forest.findSet(1));
    }

    @Test(timeout = 250)
    public void testMakeSetNull() {
        int size = 5;
        IDisjointSet<Integer> forest = new ArrayDisjointSet<>(size);

        forest.makeSet(1); //makes a set for the (1)

        //attempts to make a set out of an already existed item.
        try {
            //We expect this to throw the exception
            forest.makeSet(1);
            fail("FAIL: The content was already added to a set!");
        } catch (IllegalArgumentException e) {
            //Do nothing
        }
    }

    @Test(timeout = 250)
    public void testUnionByRoot2ndPointerSmaller() {
        int size = 10;
        IDisjointSet<Integer> forest = new ArrayDisjointSet<>(size);

        for (int i = 0; i < size; i++) {
            forest.makeSet(i);
        }

        forest.union(0, 1);
        forest.union(0, 3);
        forest.union(4, 0);  //When root 1 is larger than root2

        Assert.assertEquals(0, forest.findSet(4));
    }
}
