package Companies.Google;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ValidSquare {
    public boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
        Set<Integer> s = new HashSet<>(
                Arrays.asList(dis(p1, p2), dis(p1, p3), dis(p1, p4),
                        dis(p2, p3), dis(p2, p4), dis(p3, p4)));
        return !s.contains(0) && s.size() == 2;
    }

    private int dis(int[] a, int[] b) {
        return (a[0]-b[0])*(a[0]-b[0]) + (a[1]-b[1])*(a[1]-b[1]);
    }
}
