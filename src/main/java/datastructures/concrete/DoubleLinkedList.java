package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    /**
     * Constucts an empty double linked list.
     */
    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    /**
     * Adds the given item to the *end* of this IList.
     */
    @Override
    public void add(T item) {
        if (size() == 0) {
            front = new Node<>(null, item, null);
            back = front;
        } else {
            back.next = new Node<>(back, item, null);
            back = back.next;
        }
        size++;
    }

    /**
     * Removes and returns the item from the *end* of this IList.
     * git
     *
     * @throws EmptyContainerException if the container is empty and there is no element to remove.
     */
    @Override
    public T remove() {
        if (isEmpty()) {
            throw new EmptyContainerException("ERROR: There is no element to remove!");
        }
        T temp = back.data;
        back = back.prev;
        if (back == null) {
            front = null;
        } else {
            back.next = null;
        }
        size--;
        return temp;
    }

    /**
     * Returns the item located at the given index.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException("ERROR: Invalid Index!");
        }
        Iterator<T> iter = iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }

    /**
     * Overwrites the element located at the given index with the new item.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException("ERROR: Invalid Index!");
        }
        insert(index, item);
        delete(index + 1);
    }

    /**
     * Inserts the given item at the given index. If there already exists an element
     * at that index, shift over that element and any subsequent elements one index
     * higher.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size() + 1
     */
    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size() + 1) { // invalid index
            throw new IndexOutOfBoundsException("ERROR: Invalid Index!");
        } else if (index == size() || isEmpty()) {
            add(item);
        } else {
            Node<T> temp = goToIndex(index);
            Node<T> newNode = new Node<>(temp.prev, item, temp);
            if (newNode.prev == null) {
                front = newNode;
            } else {
                newNode.prev.next = newNode;
            }
            temp.prev = newNode;
            size++;
        }
    }

    /**
     * Deletes the item at the given index. If there are any elements located  at a higher
     * index, shift them all down by one.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public T delete(int index) {
        Node<T> temp = goToIndex(index);
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException("ERROR: Invalid Index!");
        } else if (index == 0 && this.size == 1) {
            front = null;
            back = null;
        } else if (index == 0) {
            front = temp.next;
            front.prev = null;
        } else if (index == this.size() - 1) {
            back = temp.prev;
            back.next = null;
        } else {
            temp.prev.next = temp.next;
            temp.next.prev = temp.prev;
        }
        temp.prev = null;
        temp.next = null;
        size--;
        return temp.data;
    }

    /**
     * Returns the index corresponding to the first occurrence of the given item
     * in the list.
     * <p>
     * If the item does not exist in the list, return -1.
     */
    @Override
    public int indexOf(T item) {
        Iterator<T> iter = iterator();
        T temp;
        for (int i = 0; i < this.size(); i++) { //iterate until the item is found
            temp = iter.next();
            if (item == null && temp == null || temp.equals(item)) { //check for item
                return i;
            }
        }
        return -1;  //default return
    }

    /**
     * Returns the number of elements in the container.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns 'true' if this container contains the given element, and 'false' otherwise.
     */
    @Override
    public boolean contains(T other) {
        return indexOf(other) != -1;
    }

    /**
     * Returns an iterator over the contents of this list.
     */
    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    //Helper Methods:

    /**
     * Traverses the list a given number of times.
     *
     * @param index is the number of nodes to traverse before retrieval of selected node.
     * @return the node associated with the given index of the list.
     */
    private Node<T> goToIndex(int index) {
        if (size() == 0) {
            throw new NoSuchElementException("The list is empty, you moron!");
        } else if (index == 0) { //the index is the front
            return front;
        } else if (index == this.size() - 1) { //the index is the end
            return back;
        } else if (index <= this.size() / 2) {   //the index is closer to the front
            Node<T> current = front;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {    //the index is closer to the end
            Node<T> current = back;
            int timesToTraverse = (this.size() - 1) - index;
            for (int i = 0; i < timesToTraverse; i++) {
                current = current.prev;
            }
            return current;
        }
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *                                there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("ERROR: There is no such element!");
            }

            T temp = current.data;
            current = current.next;
            return temp;
        }
    }
}