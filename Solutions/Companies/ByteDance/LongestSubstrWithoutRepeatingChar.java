package Companies.ByteDance;

import java.util.HashMap;
import java.util.Map;

public class LongestSubstrWithoutRepeatingChar {
    public int lengthOfLongestSubstring(String s) {
        int re = 0;
        Map<Character, Integer> m = new HashMap<>();
        for (int i = 0, j = 0; j < s.length(); j++) {
            if (m.containsKey(s.charAt(j))) {
                i = Math.max(i, m.get(s.charAt(j)));
            }
            re = Math.max(re, j-i+1);
            m.put(s.charAt(j), j+1);    // j+1
        }
        return re;
    }
}
