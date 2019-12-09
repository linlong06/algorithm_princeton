/* *****************************************************************************
 *  Name: Long Lin
 *  Date: 2019-10-04
 *  Description: Deque.java
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        private Item item;
        private Node nextNode;
        private Node prevNode;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Invalid Input!");
        }
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.prevNode = null;
        if (size > 1) {
            first.nextNode = oldfirst;
            oldfirst.prevNode = first;
        }
        else if (size == 1) {
            first.nextNode = last;
            last.prevNode = first;
        }
        else last = first;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Invalid Input!");
        }
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.nextNode = null;
        if (size > 1) {
            last.prevNode = oldlast;
            oldlast.nextNode = last;
        }
        else if (size == 1) {
            last.prevNode = first;
            first.nextNode = last;
        }
        else first = last;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty Deque!");
        }
        Item item = first.item;
        if (size > 1) {
            first = first.nextNode;
            first.prevNode = null;
        }
        else {
            first = null;
            last = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty Deque!");
        }
        Item item = last.item;
        if (size > 1) {
            last = last.prevNode;
            last.nextNode = null;
        }
        else {
            last = null;
            first = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("End of Deque!");
            }
            Item item = current.item;
            current = current.nextNode;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Invalid operation!");
        }
    }


    // unit testing (required)
    public static void main(String[] args) {
        // declare a new deque:
        Deque<String> deque = new Deque<String>();
        // adding elements into the deque by addFirst() and addLast() method:
        deque.addLast("Element1");
        Iterator<String> iterator = deque.iterator();
        while (iterator.hasNext()) {
            StdOut.println(iterator.next());
        }

        deque.removeFirst();
        deque.addLast("Element3");
        deque.addLast("Element4");

        // remove items from deque:
        while (!deque.isEmpty()) {
            StdOut.println("Item Removed: " + deque.removeFirst());
            StdOut.println("Remaining items: " + deque.size());
        }
    }
}
