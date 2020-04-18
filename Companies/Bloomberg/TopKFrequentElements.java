package Companies.Bloomberg;

import java.util.*;

public class TopKFrequentElements {
    public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int num : nums) {
            m.put(num, m.getOrDefault(num, 0)+1);
        }
        PriorityQueue<Integer> q = new PriorityQueue<>((a, b)->m.get(a)-m.get(b));
        for (int num : m.keySet()) {
            q.add(num);
            if (q.size() > k) {
                q.poll();
            }
        }
        List<Integer> re = new ArrayList<>();
        while (!q.isEmpty()) {
            re.add(q.poll());
        }
        Collections.reverse(re);
        return re;
    }
}
