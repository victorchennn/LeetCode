package Companies.Google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageOverlap {
    public int largestOverlap(int[][] A, int[][] B) {
        List<Integer> la = new ArrayList<>();
        List<Integer> lb = new ArrayList<>();
        int n = A.length;
        for (int i = 0; i < n*n; i++) {
            if (A[i/n][i%n] == 1) {
                la.add(i/n*100+i%n);
            }
            if (B[i/n][i%n] == 1) {
                lb.add(i/n*100+i%n);
            }
        }
        Map<Integer, Integer> m = new HashMap<>();
        for (int i : la) {
            for (int j : lb) {
                m.put(i-j, m.getOrDefault(i-j, 0)+1);
            }
        }
        int re = 0;
        for (int i : m.values()) {
            re = Math.max(re, i);
        }
        return re;
    }
}
