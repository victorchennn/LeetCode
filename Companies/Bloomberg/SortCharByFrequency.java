package Companies.Bloomberg;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @see FindMostFrequentCharinStr
 */
public class SortCharByFrequency {
    public String frequencySort(String s) {
        Map<Character, Integer> m = new HashMap<>();
        for (char c : s.toCharArray()) {
            m.put(c, m.getOrDefault(c, 0)+1);
        }
        PriorityQueue<Character> q = new PriorityQueue<>((a, b)->m.get(b)-m.get(a));
        q.addAll(m.keySet());
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll();
            for (int i = 0; i < m.get(c); i++) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
