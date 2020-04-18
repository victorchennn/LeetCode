package Companies.ByteDance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class SmallestStringWithSwaps {
    private int[] parents;

    public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        parents = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            parents[i] = i;
        }
        for (List<Integer> l : pairs) {
            union(l.get(0), l.get(1));
        }
        Map<Integer, PriorityQueue<Character>> m = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            int p = find(i);
            m.computeIfAbsent(p, k->new PriorityQueue<>()).add(s.charAt(i));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(m.get(find(i)).poll());
        }
        return sb.toString();
    }

    private void union(int x, int y) {
        parents[find(x)] = find(y);
    }

    private int find(int x) {
        if (parents[x] != x) {
            parents[x] = find(parents[x]);
        }
        return parents[x];
    }
}
