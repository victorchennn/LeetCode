package Topics.SlidingWindow;

import java.util.ArrayList;
import java.util.List;

public class FindAllAnagramsinString {

    public List<Integer> findAnagrams(String s, String p) {
        int[] count = new int[256];
        for (char c : p.toCharArray()) {
            count[c]++;
        }
        int match = 0, l = 0, r = 0;
        List<Integer> re = new ArrayList<>();
        while (r < s.length()) {
            if (count[s.charAt(r)] > 0) {
                count[s.charAt(r)]--;
                r++;
                match++;
            } else {
                count[s.charAt(l)]++;
                l++;
                match--;
            }
            if (match == p.length()) { // not match == 0
                re.add(l);
            }
        }
        return re;

    }
}
