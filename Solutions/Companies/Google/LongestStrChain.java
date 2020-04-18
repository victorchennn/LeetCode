package Companies.Google;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LongestStrChain {
    public int longestStrChain(String[] words) {
        Map<String, Integer> m = new HashMap<>();
        int re = 0;
        Arrays.sort(words, (a, b)->a.length()-b.length());
        for (String s : words) {
            int len = 0;
            for (int i = 0; i < s.length(); i++) {
                String temp = s.substring(0, i) + s.substring(i+1);
                len = Math.max(len, m.getOrDefault(temp, 0)+1);
            }
            re = Math.max(re, len);
            m.put(s, len);
        }
        return re;
    }
}
