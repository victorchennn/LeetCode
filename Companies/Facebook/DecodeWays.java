package Companies.Facebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * number of ways to form a string
 *
 * @Follow-up: Constant space?
 */
public class DecodeWays {
    public int numDecodings(String s) {
        if (s.length() == 0 || s.startsWith("0")) {
            return 0;
        }
        int[] dp = new int[s.length()+1];
        dp[0] = 1;                        // int num1 = 1;
        dp[1] = s.charAt(0) == '0'? 0:1;  // int num2 = s.charAt(0) == '0'? 0:1;
        int prepre;                       // int temp;
        for (int i = 2; i <= s.length(); i++) {
            if (s.charAt(i-1) != '0') {
                dp[i] += dp[i-1];         // temp += num2;
            }
            prepre = Integer.valueOf(s.substring(i-2, i));
            if (prepre >= 10 && prepre <= 26) {
                dp[i] += dp[i-2];        // temp += num1;
            }                            // num1 = num2;
                                         // num2 = temp;
        }
        return dp[s.length()];           // return num2;
    }

    @Test
    void test() {
        assertEquals(2, numDecodings("12"));    // "AB" (1 2) or "L" (12)
        assertEquals(3, numDecodings("226"));   // "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6)
    }
}
