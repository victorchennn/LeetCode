package Companies.VMware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LongestStrChain {
    public int longestStrChain(String[] words) {
        Map<String, Integer> m = new HashMap<>();
        int re = 0;
        Arrays.sort(words, (a,b)->a.length()-b.length());
        for (String word : words) {
            int len = 0;
            for (int i = 0; i < word.length(); i++) {
                String temp = word.substring(0, i) + word.substring(i+1);
                len = Math.max(len, m.getOrDefault(temp, 0)+1);
            }
            re = Math.max(re, len);
            m.put(word, len);
        }
        return re;
    }
}
