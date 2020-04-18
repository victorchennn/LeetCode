package Companies.Bloomberg;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FirstUniqueCharinString {
    public int firstUniqChar(String s) {
        int freq [] = new int[26];
        for(int i = 0; i < s.length(); i ++)
            freq [s.charAt(i) - 'a'] ++;
        for(int i = 0; i < s.length(); i ++)
            if(freq [s.charAt(i) - 'a'] == 1)
                return i;
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
}
