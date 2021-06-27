import java.util.Comparator;

public class IntComparator implements Comparator<Integer> {

    public int compare(Node x, Node y) {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        return Integer.compare(x.frequency, y.frequency);
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        return 0;
    }
}