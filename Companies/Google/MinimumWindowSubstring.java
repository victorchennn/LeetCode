package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class MinimumWindowSubstring {
    public String minWindow(String s, String t) {
        Map<Character, Integer> m = new HashMap<>();
        for (char c : t.toCharArray()) {
            m.put(c, m.getOrDefault(c, 0)+1);
        }
        int left = -1, min = Integer.MAX_VALUE;
        int i = 0, j = 0;
        int count = m.size();
        while (j < s.length()) {
            char c = s.charAt(j);
            m.put(c, m.getOrDefault(c, 0)-1);
            if (m.get(c) == 0) {
                count--;
            }
            while (count == 0) {
                if (min > j-i+1) {
                    min = j-i+1;
                    left = i;
                }
                char l = s.charAt(i);
                if (m.get(l) == 0) {
                    count++;
                }
                m.put(l, m.get(l)+1);
                i++;
            }
            j++;
        }
        return left == -1? "":s.substring(left, left+min);
    }
}
