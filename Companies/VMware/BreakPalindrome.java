package Companies.VMware;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BreakPalindrome {
    public String breakPalindrome(String palindrome) {
        char[] ss = palindrome.toCharArray();
        int n = ss.length;

        for (int i = 0; i < n/2; i++) {
            if (ss[i] != 'a') {
                ss[i] = 'a';
                return String.valueOf(ss);
            }
        }
        ss[n-1] = 'b';
        return n < 2? "" : String.valueOf(ss);
    }

    public String breakPalindromeII(String palindrome) {
        if (palindrome == null || palindrome.length() < 2) {
            return "";
        }
        int len = palindrome.length();
        for (int i = 0; i < len/2; i++) {
            if (palindrome.charAt(i) != 'a') {
                return palindrome.substring(0, i) + "a" + palindrome.substring(i+1);
            }
        }
        return palindrome.substring(0, len-1) + "b";
    }


    @Test
    void test() {
        assertEquals("aaccba", breakPalindrome("abccba"));
        assertEquals("", breakPalindrome("a"));
    }
}
