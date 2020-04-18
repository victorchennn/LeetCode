package Companies.Google;

import java.util.*;

public class GroupAnagrams {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> m = new HashMap<>();
        for (String s : strs) {
            char[] cs = s.toCharArray();
            Arrays.sort(cs);
            String key = new String(cs);
            if (!m.containsKey(key)) {
                m.put(key, new ArrayList<>());
            }
            m.get(key).add(s);
        }
        return new ArrayList<>(m.values());
    }
}
