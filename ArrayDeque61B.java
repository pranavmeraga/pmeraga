package deque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import java.lang.Math;

public class ArrayDeque61B<T> implements Deque61B<T> {
    private T[] item;
    private int size;
    private int front;
    private int back;
    public ArrayDeque61B() {
        item = (T[]) new Object[8];
        size = 0;
        front = 0;
        back = 0;
    }
    @Override
    public void addFirst(T x) {
        if (size == item.length) {
            resize(size * 2);
        }
        front = Math.floorMod(front - 1, item.length);
        item[front] = x;
        size++;
    }

    @Override
    public void addLast(T x) {
        if (size == item.length) {
            resize(size * 2);
        }
        item[back] = x;
        back = Math.floorMod(back + 1, item.length);
        size++;
    }

    private void resize(int x) {
        T[] a = (T[]) new Object[x];
        int i = 0;
        for (int y = 0; y < size; y++) {
            a[y] = item[front];
            front = Math.floorMod(front + 1, item.length);
        }
        item = a;
        front = 0;
        back = size;
    }
    @Override
    public List<T> toList() {
        List<T> reList = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            reList.add(item[Math.floorMod(front + x, item.length)]);
        }
        return reList;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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

        T remove = item[front];
        front = Math.floorMod(front + 1, item.length);
        size--;

        if (size > 0 && size == item.length / 4) {
            resize(item.length / 2);
        }

        return remove;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        back = Math.floorMod(back - 1, item.length);
        T remove = item[back];
        size--;

        if (size > 0 && size == item.length / 4) {
            resize(item.length / 2);
        }
        return remove;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return item[Math.floorMod(front + index, item.length)];
    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
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

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {
        private int count;

        public ArrayIterator() {
            count  = 0;
        }
        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public T next() {
            T reItem = item[(front + count) % item.length];
            count += 1;
            return reItem;
        }
    }
}
