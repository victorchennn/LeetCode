package Companies.Bloomberg;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class HandofStraights {
    public boolean isNStraightHand(int[] hand, int W) {
        PriorityQueue<Integer> q = new PriorityQueue<>();
        for (int c : hand) {
            q.add(c);
        }
        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int i = 1; i < W; i++) {
                if (!q.remove(cur+i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isNStraightHandII(int[] hand, int W) {
        Map<Integer, Integer> m = new TreeMap<>();
        for (int c : hand) {
            m.put(c, m.getOrDefault(c, 0)+1);
        }
        for (int c : m.keySet()) {
            if (m.get(c) > 0) {
                for (int i = W-1; i >= 0; i--) {
                    if (m.getOrDefault(c+i, 0) < m.get(c)) {
                        return false;
                    }
                    m.put(c+i, m.get(c+i)-m.get(c));
                }
            }
        }
        return true;
    }
}
