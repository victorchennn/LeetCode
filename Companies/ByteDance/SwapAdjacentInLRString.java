package Companies.ByteDance;

public class SwapAdjacentInLRString {
    public boolean canTransform(String start, String end) {
        if (!start.replace("X", "").equals(end.replace("X", ""))) {
            return false;
        }
        int i = 0, j = 0;
        char[] s = start.toCharArray(), e = end.toCharArray();
        while (i < start.length()) {
            while (i < s.length && s[i] == 'X') {
                i++;
            }
            while (j < s.length && e[j] == 'X') {
                j++;
            }
            if (s.length == i || j == s.length) {
                return true;
            }
            if (s[i] == 'R' && i > j) {
                return false;
            }
            if (e[j] == 'L' && i < j) {
                return false;
            }
            i++;
            j++;
        }
        return true;
    }
}
