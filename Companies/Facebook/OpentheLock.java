package Companies.Facebook;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
//            l = temp; // slow

            l = r;
            r = temp;
        }
        return -1;
    }

    @Test
    void test() {
        assertEquals(1, openLock(
                new String[]{"8888"}, "0009"));
        assertEquals(-1, openLock(
                new String[]{"0000"}, "8888"));
        assertEquals(-1, openLock(
                new String[]{"8887","8889","8878","8898","8788","8988","7888","9888"}, "8888"));
        assertEquals(6, openLock(
                new String[]{"0201","0101","0102","1212","2002"}, "0202"));

    }
}
