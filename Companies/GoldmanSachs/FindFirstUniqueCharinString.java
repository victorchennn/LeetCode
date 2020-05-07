package Companies.GoldmanSachs;

import java.util.*;

public class FindFirstUniqueCharinString {
    public int firstUniqChar(String s) {
        int[] freq = new int[26];
        for(int i = 0; i < s.length(); i ++) {
            freq[s.charAt(i)-'a'] ++;
        }
        for(int i = 0; i < s.length(); i ++) {
            if(freq[s.charAt(i)-'a'] == 1) {
                return i;
            }
        }
        return -1;
    }

    public int firstUniqCharII(String s) {
        Map<Character, Integer> m = new LinkedHashMap<>();
        Set<Character> set = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            if (set.contains(s.charAt(i))) {
                m.remove(s.charAt(i));
            } else {
                m.put(s.charAt(i), i);
                set.add(s.charAt(i));
            }
        }
        return m.size() == 0? -1:m.entrySet().iterator().next().getValue();
    }

    public int firstUniqCharIII(String s) {
        Map<Character, Integer> feq = new HashMap<>();
        Map<Character, Integer> index = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            feq.put(s.charAt(i), feq.getOrDefault(s.charAt(i), 0) + 1);
            index.put(s.charAt(i), i);
        }

        int min = Integer.MAX_VALUE;
        for (char c : feq.keySet()) {
            if (feq.get(c) == 1) {
                min = Math.min(min, index.get(c));
            }
        }

        return min == Integer.MAX_VALUE ? -1 : min;
    }
}
