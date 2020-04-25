package Companies.Bloomberg;

public class PalindromeValid {
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
            }
            l++;
            r--;
        }
        return true;
    }

    public boolean isPalindromeII(String s) {
        StringBuilder sb = new StringBuilder();

        s.chars()
                .filter(c -> Character.isLetterOrDigit(c))
                .mapToObj(c -> Character.toLowerCase((char) c))
                .forEach(sb::append);

        return sb.toString().equals(sb.reverse().toString());
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
