package Companies.Microsoft;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Return the minimum number of steps to make s palindrome.
 *
 * @see Companies.Bloomberg.ValidPalindrome
 */
public class MinimumInsertionStepstoMakeStringPalindrome {
    public int minInsertions(String s) {
        int len = s.length();
        int[][] dp = new int[len][len];
        for (int r = 1; r < len; r++) {
            for (int l = r-1; l >= 0; l--) {
                if (s.charAt(l) == s.charAt(r)) {
                    if (r-l <= 1) {
                        dp[l][r] = 0;
                    } else {
                        dp[l][r] = dp[l+1][r-1];
                    }
                } else {
                    dp[l][r] = Math.min(dp[l+1][r], dp[l][r-1])+1;
                }
            }
        }
        return dp[0][len-1];
    }

    @Test
    void test() {
        assertEquals(0, minInsertions("zzazz"));
        assertEquals(2, minInsertions("mbadm"));
        assertEquals(5, minInsertions("leetcode"));
        assertEquals(0, minInsertions("g"));
        assertEquals(1, minInsertions("no"));
    }
}
