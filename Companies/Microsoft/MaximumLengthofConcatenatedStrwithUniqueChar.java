package Companies.Microsoft;

import java.util.List;

public class MaximumLengthofConcatenatedStrwithUniqueChar {
    public int maxLength(List<String> arr) {
        return dfs(arr, 0, "");
    }

    private int dfs(List<String> arr, int start, String s) {
        if (!unique(s)) {
            return 0;
        }
        int re = s.length();
        for (int i = start; i < arr.size(); i++) {
            re = Math.max(re, dfs(arr, i+1, s+arr.get(i)));
        }
        return re;
    }

    private boolean unique(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            if (count[c-'a'] != 0) {
                return false;
            }
            count[c-'a']++;
        }
        return true;
    }
}
