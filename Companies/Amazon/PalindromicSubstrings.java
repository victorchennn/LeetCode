package Companies.Amazon;

/**
 * @see LongestPalindromicSubstring
 */
public class PalindromicSubstrings {
    public int countSubstrings(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            count += helper(s, i, i); // odd length
            count += helper(s, i, i+1); // even length
        }
        return count;
    }

    private int helper(String s, int l, int r) {
        int count = 0;
        while (l >= 0 && r < s.length() && (s.charAt(l) == s.charAt(r))) {
            l--;
            r++;
            count++;
        }
        return count;
    }
}
