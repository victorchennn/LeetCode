package Topics.SlidingWindow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
            m.put(c, r+1); // r+1 !!!!!
        }
        return re;
    }

    public int lengthOfLongestSubstringII(String s) {
        int re = 0, l = 0, r =0;
        Set<Character> set = new HashSet<>();
        while (r < s.length()) {
            if (!set.contains(s.charAt(r))) {
                set.add(s.charAt(r++));
                re = Math.max(re, r-l);
            } else {
                set.remove(s.charAt(l++));
            }
        }
        return re;
    }

    public static void main(String...args) {
        LongestSubstringWithoutRepeatingChar test = new LongestSubstringWithoutRepeatingChar();
        test.lengthOfLongestSubstringII("abcabcbb");
    }
}
