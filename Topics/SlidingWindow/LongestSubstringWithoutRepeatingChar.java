package Topics.SlidingWindow;

import java.util.HashMap;
import java.util.Map;

public class LongestSubstringWithoutRepeatingChar {
    /**
     * Input: "abcabcbb"
     * Output: 3
     * Explanation: The answer is "abc", with the length of 3.
     *
     * Space complexity : O(min(m,n)).
     * We need O(k) space for the sliding window, where k is the size of the map.
     * The size of the map is upper bounded by the size of the string n and the size of the charset/alphabet m.
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

    /**
     * int[26] for Letters 'a' - 'z' or 'A' - 'Z'
     * int[128] for ASCII
     * int[256] for Extended ASCII
     *
     * Space complexity (Table): O(m). m is the size of the charset.
     */
    public int lengthOfLongestSubstringII(String s) {
        int[] index = new int[128]; // current index of character
        // try to extend the range [l, r]
        int re = 0, l = 0;
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            l = Math.max(index[c], l);
            re = Math.max(re, r - l + 1);
            index[c] = r + 1;
        }
        return re;
    }
}
