package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class BeautifulArray {
    Map<Integer, int[]> m;

    public int[] beautifulArray(int N) {
        m = new HashMap<>();
        return f(N);
    }

    private int[] f(int n) {
        if (m.containsKey(n)) {
            return m.get(n);
        }
        int[] re = new int[n];
        if (n == 1) {
            return new int[]{1};
        } else {
            int t = 0;
            for (int x : f((n+1)/2)) {
                re[t++] = 2*x-1;
            }
            for (int x : f(n/2)) {
                re[t++] = 2*x;
            }
        }
        m.put(n, re);
        return re;
    }
}
