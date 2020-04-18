package Companies.Bloomberg;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class CheapestFlightsWithinKStops {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, Map<Integer, Integer>> m = new HashMap<>();
        for (int[] flight : flights) {
            m.computeIfAbsent(flight[0], k->new HashMap<>()).put(flight[1], flight[2]);
        }
        PriorityQueue<int[]> q = new PriorityQueue<>((a, b)->a[0]-b[0]);
        q.add(new int[]{0, src, K});
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int price = cur[0], position = cur[1], step = cur[2];
            if (position == dst) {
                return price;
            }
            if (step >= 0 && m.containsKey(position)) {
                Map<Integer, Integer> next = m.get(position);
                for (int np : next.keySet()) {
                    q.add(new int[]{price+next.get(np), np, step-1});
                }
            }
        }
        return -1;
    }
}
