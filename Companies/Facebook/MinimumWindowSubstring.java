package Companies.Facebook;

import java.util.HashMap;
import java.util.Map;

public class MinimumWindowSubstring {
    public String minWindow(String s, String t) {
        Map<Character, Integer> m = new HashMap<>();
        for (char c : t.toCharArray()) {
            m.put(c, m.getOrDefault(c, 0)+1);
        }
        int l = 0, r = 0, dis = Integer.MAX_VALUE, left = -1, count = m.size();
        while (r < s.length()) {
            char c = s.charAt(r);
            m.put(c, m.getOrDefault(c, 0)-1);
            if (m.get(c) == 0) {
                count--;
            }
            while (count == 0) {
                if (dis > r-l+1) {
                    dis = r-l+1;
                    left = l;
                }
                char begin = s.charAt(l);
                if (m.get(begin) == 0) {  // first check, then add 1
                    count++;
                }
                m.put(begin, m.get(begin)+1);
                l++;
            }
            r++;
        }
        return left == -1? "":s.substring(left, left+dis);
    }
}
