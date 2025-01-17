package deque;
import java.util.Comparator;

public class MaxArrayDeque61B<T> extends ArrayDeque61B<T> {
    private Comparator<T> comp;
    public MaxArrayDeque61B(Comparator<T> c) {
        super();
        comp = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }
        T m = get(0);
        for (int i = 1; i < size(); i++) {
            T z = get(i);
            if (comp.compare(z, m) > 0) {
                m = z;
            }
        }
        return m;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T m = get(0);
        for (int i = 1; i < size(); i++) {
            T z = get(i);
            if (c.compare(z, m) > 0) {
                m = z;
            }
        }
        return m;
    }
}
