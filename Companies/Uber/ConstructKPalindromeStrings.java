package Companies.Uber;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Return True if you can use all the characters in s to construct
 * k palindrome strings or False otherwise.
 */
public class ConstructKPalindromeStrings {
    public boolean canConstruct(String s, int k) {
        if (k > s.length()) {
            return false;
        }
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c-'a']++;
        }
        int odd = 0;
        for (int num : count) {
            if (num%2 == 1) {
                odd++;
            }
        }
        return odd <= k;
    }

    @Test
    void test() {
        assertEquals(true, canConstruct("annabelle",2));
        assertEquals(false, canConstruct("leetcode",3));
        assertEquals(true, canConstruct("true",4));
        assertEquals(true, canConstruct("yzyzyzyzyzyzyzy",2));
        assertEquals(false, canConstruct("cr",7));
    }
}
