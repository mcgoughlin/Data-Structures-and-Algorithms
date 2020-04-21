import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    // first denotes the first node in linked list
    private Node first = null;
    // last denotes the last node in linked list
    private Node last = null;
    // size of linked list
    private int size;

    private class Node {
        // item will be the generic stored value
        private Item item;
        // next will point to the next node in sequence
        private Node next;
        // previous will point to the previous node in sequence
        private Node previous;
    }


    // construct an empty deque - using ugly cast if array
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        if (oldfirst != null) {
            oldfirst.previous = first;
        }
        last = connect(last, first);
        size++;
    }

    // check if first/last node existent upon the others' creation, connect them if not
    private Node connect(Node node1, Node node2) {
        if (node1 == null) {
            return node2;
        }
        else {
            return node1;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newlast = new Node();
        newlast.item = item;
        if (last != null) {
            last.next = newlast;
            newlast.previous = last;
        }
        last = newlast;
        first = connect(first, last);
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Node oldfirst = first;
        if (first != last) {
            first = oldfirst.next;
            oldfirst.previous = null;
        }
        else {
            first = null;
            last = null;
        }
        size--;
        return oldfirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Node oldlast = last;
        if (first != last) {
            last = oldlast.previous;
            last.next = null;
        }
        else {
            first = null;
            last = null;
        }
        size--;
        return oldlast.item;
    }

    // return an iterator over items in order from front to back
    // has a next(), remove() and hasNext() function
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node current = first;

            public boolean hasNext() {
                return (current != null);
            }

            public Item next() {
                if (hasNext()) {
                    Item item = current.item;
                    current = current.next;
                    return item;
                }
                else {
                    throw new java.util.NoSuchElementException();
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // unit testing (required)
    public static void main(String[] args) {

        StdOut.println("Autograder Start:");
        Deque<Integer> deque = new Deque<Integer>();
        deque.addLast(1);
        deque.removeFirst();
        deque.addFirst(3);
        deque.addLast(4);
        StdOut.println("Autograder End.");
        StdOut.println(" ");


        deque.removeLast();

        deque.isEmpty();
        deque.size();
        deque.isEmpty();
        if (deque.iterator().hasNext()) {
            deque.iterator().next();
        }
        deque.removeFirst();
        deque.removeLast();
        deque.iterator().remove();

    }

}
