package Companies.Google;

import java.util.Stack;
import java.util.TreeSet;

public class BraceExpansion {
    public String[] expand(String S) {
        TreeSet<String> set = new TreeSet<>();
        if (S.length() == 0) {
            return new String[]{""};
        }
        if (S.length() == 1) {
            return new String[]{S};
        }
        if (S.charAt(0) == '{') {
            int i = 1;
            while (S.charAt(i) != '}') {
                i++;
            }
            String s = S.substring(1, i);
            String[] before = s.split(",");
            String[] after = expand(S.substring(i+1));
            for (String be : before) {
                for (String af : after) {
                    set.add(be+af);
                }
            }
        } else {
            String[] after = expand(S.substring(1));
            for (String af : after) {
                set.add(S.charAt(0)+af);
            }
        }
        Stack<Integer> s = new Stack<>();
        return set.toArray(new String[1]);
    }
}
