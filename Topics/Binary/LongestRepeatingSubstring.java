package Topics.Binary;

import java.util.HashSet;

public class LongestRepeatingSubstring {
    public int longestRepeatingSubstring(String S) {
        int l = 0, r = S.length();
        while (l <= r) {
            int m = l + (r-l)/2;
            if (helper(m, S) != -1) {
                l = m+1;
            } else {
                r = m-1;
            }
        }
        return l;
    }

    private int helper(int m, String s) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < s.length()-m; i++) {
            String temp = s.substring(i, i+m+1);
            int h = temp.hashCode(); // save memory, may have large strings
            if (set.contains(h)) {
                return i;
            }
            set.add(h);
        }
        return -1;
    }
}
