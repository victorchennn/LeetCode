package Companies.Google;

import java.util.Arrays;

public class CompareStrbyFreqofSmallestChar {
    public int[] numSmallerByFrequency(String[] queries, String[] words) {
        int[] qs = new int[queries.length];
        int[] ws = new int[words.length];
        for (int i = 0; i < queries.length; i++) {
            qs[i] = helper(queries[i]);
        }
        for (int i = 0; i < words.length; i++) {
            ws[i] = helper(words[i]);
        }
        Arrays.sort(ws);
        int[] re = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int l = 0, r = ws.length;
            while (l < r) {
                int mid = l + (r-l)/2;
                if (ws[mid] > qs[i]) {
                    r = mid;
                } else {
                    l = mid+1;
                }
            }
            re[i] = words.length-l;
        }
        return re;
    }

    private int helper(String s) {
        int[] ch = new int[26];
        for (char c : s.toCharArray()) {
            ch[c-'a']++;
        }
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] != 0) {
                return ch[i];
            }
        }
        return 0;
    }
}
