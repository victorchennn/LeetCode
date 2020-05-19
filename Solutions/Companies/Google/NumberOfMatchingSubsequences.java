package Companies.Google;

import java.util.ArrayList;
import java.util.List;

public class NumberofMatchingSubsequences {

    public int numMatchingSubseq(String S, String[] words) {
        List<StringBuilder>[] l = new List[26];
        for (int i = 0; i < l.length; i++) {
            l[i] = new ArrayList<>();
        }
        for (String s : words) {
            l[s.charAt(0)-'a'].add(new StringBuilder(s));
        }
        int re = 0;
        for (char c : S.toCharArray()) {
            List<StringBuilder> cur = l[c-'a'];
            l[c-'a'] = new ArrayList<>();
            for (StringBuilder sb : cur) {
                sb.deleteCharAt(0);
                if (sb.length() != 0) {
                    l[sb.charAt(0)-'a'].add(sb);
                } else {
                    re++;
                }
            }
        }
        return re;
    }
}
