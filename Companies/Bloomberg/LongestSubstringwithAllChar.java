package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Find longest substring in s which contains all chars in string t
 */
public class LongestSubstringwithAllChar {
    public static String maxWindow(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        }
        String re = "";
        Map<Character, Integer> m = new HashMap<>();
        for (char c : t.toCharArray()) {
            m.put(c, m.getOrDefault(c, 0)+1);
        }
        int l = 0, r = 0, max = 0, unique = m.size();
        while (r < s.length()) {
            char c = s.charAt(r);
            if (!m.containsKey(c)) {
                if (unique == 0 && r-l+1 > max) {
                    max = r-l+1;
                    re = s.substring(l, r+1);
                }
                r++;
                continue;
            }
            if (m.get(c) == 0) {
                while (s.charAt(l) != c) {
                    l++;
                }
                l++;
            } else {
                m.put(c, m.get(c)-1);
                if (m.get(c) == 0) {
                    unique--;
                    if (unique == 0) {
                        if (r-l+1 > max) {
                            max = r-l+1;
                            re = s.substring(l, r+1);
                        }
                    }
                }
            }
            r++;
        }
        return re;
    }

    @Test
    void test() {
        assertEquals("abc", maxWindow("abc", "abc"));
        assertEquals("abcd", maxWindow("abcd", "abc"));
        assertEquals("dabc", maxWindow("dabc", "abc"));
        assertEquals("adbc", maxWindow("adbc", "abc"));
        assertEquals("", maxWindow("abc", "abcc"));
        assertEquals("cabc", maxWindow("cabc", "abcc"));
        assertEquals("ccab", maxWindow("ccabc", "abcc"));
        assertEquals("ADOBECODE", maxWindow("ADOBECODEBANC", "ABC"));
        assertEquals("DOABECODE", maxWindow("DOABECODEBANC", "ABC"));
    }
}
