package Companies.Google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimalAccountBalancing {
    public int minTransfers(int[][] transactions) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int[] tran : transactions) {
            m.put(tran[0], m.getOrDefault(tran[0], 0) - tran[2]);
            m.put(tran[1], m.getOrDefault(tran[1], 0) + tran[2]);
        }
        return helper(0, new ArrayList<>(m.values()));
    }

    private int helper(int start, List<Integer> l) {
        while (start < l.size() && l.get(start) == 0) {
            start++;
        }
        if (start == l.size()) {
            return 0;
        }
        int re = Integer.MAX_VALUE;
        for (int i = start+1; i < l.size(); i++) {
            if (l.get(start) * l.get(i) < 0) {
                l.set(i, l.get(i)+l.get(start));
                re = Math.min(re, 1 + helper(start+1, l));
                l.set(i, l.get(i)-l.get(start));
            }
        }
        return re;
    }
}
