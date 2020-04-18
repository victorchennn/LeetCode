package Companies.Bloomberg;

import java.util.HashMap;
import java.util.Map;

public class LongestSubstringWithoutRepeatingChar {

    /**
     * Input: "abcabcbb"
     * Output: 3
     * Explanation: The answer is "abc", with the length of 3.
     */
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> m = new HashMap<>();
        int re = 0, l = 0;
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            if (m.containsKey(c)) {
                l = Math.max(l, m.get(c));
            }
            re = Math.max(re, r-l+1);
            m.put(c, r+1);
        }
        return re;
    }
}
