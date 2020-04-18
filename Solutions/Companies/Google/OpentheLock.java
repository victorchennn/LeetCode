package Companies.Google;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OpentheLock {
    public int openLock(String[] deadends, String target) {
        Set<String> l = new HashSet<>();
        Set<String> r = new HashSet<>();
        Set<String> deads = new HashSet<>(Arrays.asList(deadends));
        l.add("0000");
        r.add(target);
        int step = 0;
        while (!l.isEmpty() && !r.isEmpty()) {
            Set<String> temp = new HashSet<>();
            for (String s : l) {
                if (r.contains(s)) {
                    return step;
                }
                if (deads.contains(s)) {
                    continue;
                }
                deads.add(s);
                for (int i = 0; i < 4; i++) {
                    char c = s.charAt(i);
                    String s1 = s.substring(0, i) + (c=='9'?0:c-'0'+1) + s.substring(i+1);
                    String s2 = s.substring(0, i) + (c=='0'?9:c-'0'-1) + s.substring(i+1);
                    temp.add(s1);
                    temp.add(s2);
                }
            }
            step++;
            l = r;
            r = temp;
        }
        return -1;
    }

    public static void main(String...args) {
        OpentheLock test = new OpentheLock();
        test.openLock(new String[]{"8888"}, "0009");
    }
}
