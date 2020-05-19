package Companies.Bloomberg;

import java.util.*;

public class GroupAnagrams {
    /**
     * Time Complexity: O(NKlogK)
     * N is the length of strs
     * K is the maximum length of a string in strs.
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> m = new HashMap<>();
        for (String s : strs) {
            char[] cs = s.toCharArray();
            Arrays.sort(cs);
            m.computeIfAbsent(new String(cs), k->new ArrayList<>()).add(s);
        }
        return new ArrayList<>(m.values());
    }

    /* Time Complexity: O(NK) */
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
