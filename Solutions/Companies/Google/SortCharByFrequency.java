package Companies.Google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortCharByFrequency {
    public String frequencySort(String s) {
        Map<Character, Integer> m = new HashMap<>();
        for (char c : s.toCharArray()) {
            m.put(c, m.getOrDefault(c, 0)+1);
        }
        List<Character>[] l = new List[s.length()+1];
        for (char c : m.keySet()) {
            int f = m.get(c);
            if (l[f] == null) {
                l[f] = new ArrayList<>();
            }
            l[f].add(c);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = l.length-1; i >= 0; i--) {
            if (l[i] != null) {
                for (char c : l[i]) {
                    for (int j = 0; j < i; j++) {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }
}
