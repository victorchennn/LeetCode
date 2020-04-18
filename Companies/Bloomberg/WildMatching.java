package Companies.Bloomberg;

public class WildMatching {

    /**
     * '?' Matches any single character.
     * '*' Matches any sequence of characters (including the empty sequence).
     */
    public boolean isMatch(String s, String p) {
        int i = 0, j = 0, ss = 0, ps = -1;
        while (i < s.length()) {
            if (j < p.length() && (s.charAt(i) == p.charAt(j) || p.charAt(j) == '?')) {
                i++;
                j++;
            } else if (j < p.length() && p.charAt(j) == '*') {
                ss = i;
                ps = j;
                j++;
            } else if (ps >= 0) {
                i = ss+1;
                j = ps;
            } else {
                return false;
            }
        }
        while (j < p.length() && p.charAt(j) == '*') {
            j++;
        }
        return j == p.length();
    }
}
