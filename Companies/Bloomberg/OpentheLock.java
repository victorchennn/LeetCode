package Companies.Bloomberg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OpentheLock {
    public int openLock(String[] deadends, String target) {
        Set<String> l = new HashSet<>(Arrays.asList("0000"));
        Set<String> r = new HashSet<>(Arrays.asList(target));
        Set<String> deads = new HashSet<>(Arrays.asList(deadends));

        int step = 0;
        while (!l.isEmpty() && !r.isEmpty()) {
            Set<String> temp = new HashSet<>();
            for (String str : l) {
                if (r.contains(str)) {
                    return step;
                }
                if (deads.contains(str)) {
                    continue;
                }
                deads.add(str);
                for (int i = 0; i < 4; i++) {
                    char c = str.charAt(i);
                    String s1 = str.substring(0, i) + (c=='0'?9:c-'0'-1) + str.substring(i+1);
                    String s2 = str.substring(0, i) + (c=='9'?0:c-'0'+1) + str.substring(i+1);
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
