/* *****************************************************************************
 *  Name: Long Lin
 *  Date: 2019-10-08
 *  Description: RandomizedQueue.java
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        array = (Item[]) new Object[0];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // resize the array
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++)
            copy[i] = array[i];
        array = copy;
    }


    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Invalid input!");
        }
        if (isEmpty()) {
            resize(1);
            array[0] = item;
        }
        else {
            if (size == array.length) {
                resize(2 * array.length);
            }
            array[size] = item;
        }
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty Queue!");
        }
        if (size > 0 && size == array.length / 4) {
            resize(array.length / 2);
        }
        int index = StdRandom.uniform(size);
        Item item = array[index];
        array[index] = array[size - 1];
        array[size - 1] = null;
        size--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty Queue!");
        }
        int index = StdRandom.uniform(size);
        return array[index];

    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private int index = 0;
        private final Item[] randArray = (Item[]) new Object[size];

        private QueueIterator() {
            for (int i = 0; i < size; i++) {
                randArray[i] = array[i];
            }
            StdRandom.shuffle(randArray);
        }

        public boolean hasNext() {
            return randArray.length > 0 && index < randArray.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("End of Queue!");
            }
            Item item = randArray[index];
            index++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Invalid operation!");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        // declare a new deque:
        RandomizedQueue<String> randqueue = new RandomizedQueue<String>();
        // adding elements into the deque by addFirst() and addLast() method:
        randqueue.enqueue("Element1");
        randqueue.enqueue("Element2");
        randqueue.enqueue("Element3");
        randqueue.enqueue("Element4");
        randqueue.enqueue("Element5");
        randqueue.enqueue("Element6");
        StdOut.println("Array size: " + randqueue.size());
        StdOut.println("Item Removed: " + randqueue.dequeue());
        StdOut.println("Array size: " + randqueue.size());
        StdOut.println("Item Sampled: " + randqueue.sample());
        StdOut.println("Array size: " + randqueue.size());

        // print out all elments in order by iterator
        Iterator<String> iterator = randqueue.iterator();
        while (iterator.hasNext()) {
            StdOut.println(iterator.next());
        }
        StdOut.println("Array size: " + randqueue.size());

        // remove items from deque:
        while (!randqueue.isEmpty()) {
            StdOut.println("Item Removed: " + randqueue.dequeue());
            StdOut.println("Remaining items: " + randqueue.size());
        }

    }

}
