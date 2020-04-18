package Companies.Microsoft;

public class ValidPalindrome {
    public boolean isPalindrome(String s) {
        int l = 0, r = s.length()-1;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) {
                l++;
            }
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) {
                r--;
            }
            if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) {
                return false;
            } else {
                l++;
                r--;
            }
        }
        return true;
    }

    /**
     * may delete at most one character.
     */
    public boolean validPalindrome(String s) {
        int l = 0, r = s.length()-1;
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return isP(s, l+1, r) || isP(s, l, r-1);
            }
            l++;
            r--;
        }
        return true;
    }

    private boolean isP(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }
}
