package Companies.Microsoft;

import java.util.*;

public class GroupAnagrams {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> m = new HashMap<>();
        for (String s : strs) {
            char[] cs = s.toCharArray();
            Arrays.sort(cs);
            m.computeIfAbsent(new String(cs), k->new ArrayList<>()).add(s);
        }
        return new ArrayList<>(m.values());
    }

    public List<List<String>> groupAnagramsII(String[] strs) {
        Map<String, List<String>> m = new HashMap<>();
        for (String s : strs) {
            int[] count = new int[26];
            for(char c : s.toCharArray()) {
                count[c-'a']++;
            }
            StringBuilder sb = new StringBuilder();
            for (int freq : count) {
                sb.append(freq);
            }
            m.computeIfAbsent(sb.toString(), k->new ArrayList<>()).add(s);
        }
        return new ArrayList<>(m.values());
    }
}
