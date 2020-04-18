package Companies.Bloomberg;

import java.util.*;

public class TopKFrequentWords {
    public List<String> topKFrequent(String[] words, int k) {
        // Map<String, Integer> m = new HashMap<>();
        // for (String word : words) {
        //     m.put(word, m.getOrDefault(word, 0)+1);
        // }
        // PriorityQueue<String>[] p = new PriorityQueue[words.length+1];
        // for (String word : m.keySet()) {
        //     if (p[m.get(word)] == null) {
        //         p[m.get(word)] = new PriorityQueue<>();
        //     }
        //     p[m.get(word)].add(word);
        // }
        // List<String> re = new ArrayList<>();
        // for (int i = p.length-1; i >= 0; i--) {
        //     while (p[i] != null && !p[i].isEmpty() && k > 0) {
        //         re.add(p[i].poll());
        //         k--;
        //     }
        // }
        // return re;

        Map<String, Integer> count = new HashMap<>();
        for (String word: words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }
        PriorityQueue<String> heap = new PriorityQueue<String>(
                (w1, w2) -> count.get(w1).equals(count.get(w2)) ?
                        w2.compareTo(w1) : count.get(w1) - count.get(w2) );

        for (String word: count.keySet()) {
            heap.offer(word);
            if (heap.size() > k) heap.poll();
        }

        List<String> ans = new ArrayList<>();
        ans.addAll(heap);
        while (!heap.isEmpty()) ans.add(heap.poll());
        Collections.reverse(ans);
        return ans;
    }
}
