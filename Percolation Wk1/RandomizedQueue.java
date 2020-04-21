import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

// Invariant - tail should always be null, waiting to be filled.

public class RandomizedQueue<Item> implements Iterable<Item> {
    // Array capacity
    private int capacity = 4;
    // Count furthest forward point containing objects
    private int head = capacity - 1;
    // Count furthest forward point containing objects
    private int tail = capacity - 1;
    // array that structures Randomized queue
    private Item[] a;


    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return a[head] == null;
    }

    // resizes array, b=true when getting larger, b=false when getting smaller
    private void resize(boolean b) {
        int size = size();
        Item[] oldA = a;
        int idx = 0;
        // Getting larger
        if (b) {
            capacity = capacity * 2;
            int newstart = capacity - size;
            a = (Item[]) new Object[capacity];
            for (Item i : oldA) {
                if (i != null) {
                    a[newstart + idx] = i;
                }
                idx++;
            }
            head = capacity - 1;
            tail = head - size;
        }
        // Getting smaller
        else {
            capacity = capacity / 2;
            head = capacity - 1;
            int oldtail = tail;
            tail = head - size;
            a = (Item[]) new Object[capacity];
            while (idx < size) {
                a[tail + idx + 1] = oldA[oldtail + idx + 1];
                idx++;
            }
        }
    }

    // Generates a random number index to choose any of the array items
    private int randIndexGen() {
        return StdRandom.uniform(tail + 1, head + 1);
    }

    // Returns a random item from array
    private Item itemReturner() {

        if (tail + 1 != head) {
            int i = randIndexGen();
            Item intermediary = a[i];
            a[i] = a[head];
            a[head] = null;
            head--;
            return intermediary;
        }
        else {
            int i = head;
            Item intermediary = a[i];
            a[head] = null;
            tail = head;
            return intermediary;
        }
    }

    // return the number of items on the randomized queue
    public int size() {
        return head - tail;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (tail == -1) {
            resize(true);
        }
        a[tail] = item;
        tail--;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size() == 0) {
            throw new java.util.NoSuchElementException();
        }
        if (size() <= (0.25 * capacity)) {
            resize(false);
        }
        return itemReturner();
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0) {
            throw new java.util.NoSuchElementException();
        }
        return a[randIndexGen()];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            public boolean hasNext() {
                return a[head] != null;
            }

            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return itemReturner();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // unit testing (required)
    public static void main(String[] args) {

        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(3);
        rq.dequeue();
        rq.enqueue(164);
        rq.enqueue(281);
        rq.size();
        rq.dequeue();
        rq.size();
        rq.enqueue(27);
        rq.sample();


        // rq.enqueue("Middle");
        // rq.enqueue("hello");
        // rq.enqueue("1");
        // rq.enqueue("2");
        // rq.enqueue("5");
        rq.sample();
        rq.isEmpty();
        rq.size();
        rq.isEmpty();
        // rq.enqueue("3");
        rq.size();
        if (rq.iterator().hasNext()) {
            rq.iterator().next();
        }
        // rq.enqueue("3");
        rq.dequeue();
        // rq.iterator().remove();
    }

}
