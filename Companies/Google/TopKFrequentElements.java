package Companies.Google;

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

    /* Bucket Sorting */
    public int[] topKFrequentII(int[] nums, int k) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int num : nums) {
            m.put(num, m.getOrDefault(num, 0)+1);
        }
        List<Integer>[] bucket = new List[nums.length+1];
        for (int key : m.keySet()) {
            if (bucket[m.get(key)] == null) {
                bucket[m.get(key)] = new ArrayList<>();
            }
            bucket[m.get(key)].add(key);
        }
        int[] re = new int[k];
        int index = 0;
        for (int i = nums.length; i >= 0; i--) {
            if (bucket[i] != null) {
                for (int e : bucket[i]) {
                    re[index++] = e;
                    if (index >= k) {
                        return re;
                    }
                }
            }
        }
        return re;
    }
}
