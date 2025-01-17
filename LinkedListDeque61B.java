package deque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedListDeque61B<T> implements Deque61B<T> {
    private Node sentinel;
    private int size;

    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public LinkedListDeque61B() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node count;

        public LinkedListIterator() {
            count  = sentinel.next;
        }
        @Override
        public boolean hasNext() {
            return count != sentinel;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                return null;
            }
            T reItem = count.item;
            count = count.next;
            return reItem;
        }
    }

    @Override
    public void addFirst(T x) {
        Node newN = new Node(x, sentinel, sentinel.next);
        sentinel.next.prev = newN;
        sentinel.next = newN;
        size++;
    }

    @Override
    public void addLast(T x) {
        Node newN = new Node(x, sentinel.prev, sentinel);
        sentinel.prev.next = newN;
        sentinel.prev = newN;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node c = sentinel.next;
        while (c != sentinel) {
            returnList.add(c.item);
            c = c.next;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return sentinel == sentinel.next;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node v = sentinel.next;
        sentinel.next = v.next;
        v.next.prev = sentinel;
        size--;
        return v.item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node v = sentinel.prev;
        sentinel.prev = v.prev;
        v.prev.next = sentinel;
        size--;
        return v.item;
    }

    @Override
    public T get(int index) {
        if (index - 1 > size || index < 0) {
            return null;
        }
        Node w = sentinel.next;
        for (int i = 0; i <= index - 1; i++) {
            w = w.next;
        }
        return w.item;
    }

    @Override
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return gethelp(sentinel.next, index);
    }

    private T gethelp(Node n, int i) {
        if (i == 0) {
            return n.item;
        }
        return gethelp(n.next, i - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Deque61B<?>) {
            Deque61B<?> other = (Deque61B<?>) o;
            if (this.size != other.size()) {
                return false;
            }
            int c = 0;
            for (T x : this) {
                if (x == null || !x.equals(other.get(c))) {
                    return false;
                }
                c += 1;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String returnString = "[" + this.get(0);
        for (int i = 1; i < this.size; i += 1) {
            returnString += ", ";
            returnString += this.get(i);
        }
        returnString += "]";
        return returnString;
    }
}
