package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindDeepestString {
    public String find(String s) {
        int counter = 0, max = 0, l = -1;
        String re = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') {
                counter++;
                if (counter > max) {
                    max = counter;
                    l = i;
                }
            } else if (c == '}') {
                if (counter == max) {
                    re = s.substring(l+1, i);
                }
                counter--;
            }
        }
        if (l == -1) {
            return s;
        }
        return re;
    }

    /* Find all deepest string */
    public List<String> findII(String s) {
        List<String> re = new ArrayList<>();
        int counter = 0, max = 0, l = -1;
        for (char c : s.toCharArray()) {
            if (c == '{') {
                counter++;
                max = Math.max(max, counter);
            } else if (c == '}') {
                counter--;
            }
        }
        if (max == 0) {
            return new ArrayList<>(Arrays.asList(s));
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') {
                counter++;
                if (counter == max) {
                    l = i;
                }
            } else if (c == '}') {
                if (counter == max) {
                    re.add(s.substring(l+1, i));
                }
                counter--;
            }
        }
        return re;
    }

    /* Find all nested string */
    public List<String> findIII(String s) {
        List<String> re = new ArrayList<>();
        Map<Integer, List<String>> m = new HashMap<>();
        int[] pos = new int[s.length()];
        Arrays.fill(pos, -1);
        int counter = 0, max = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') {
                counter++;
                max = Math.max(max, counter);
                pos[counter] = i;
            } else if (c == '}') {
                m.computeIfAbsent(counter, k->new ArrayList<>()).add(s.substring(pos[counter]+1, i));
                counter--;
            }
        }
        return m.size() == 0? new ArrayList<>(Arrays.asList(s)):m.get(max);
    }

    @Test
    void test() {
        assertEquals("123", find("123"));
        assertEquals("123", find("{123}"));
        assertEquals("4", find("1{2{3{4}}5}}6"));
        assertEquals("444", find("{{{444}}{}{}}"));

        assertEquals(true, findII("{{{444}}{}{}}").equals(
                new ArrayList<>(Arrays.asList("444"))));
        assertEquals(true, findII("{{{222}}{}{{333}}}").equals(
                new ArrayList<>(Arrays.asList("222","333"))));
        assertEquals(true, findII("{{{222}}{}{{{}333}}}").equals(
                new ArrayList<>(Arrays.asList(""))));
        assertEquals(true, findII("{{{222}}{}{{{4}333}}}").equals(
                new ArrayList<>(Arrays.asList("4"))));

        assertEquals(true, findIII("123").equals(
                new ArrayList<>(Arrays.asList("123"))));
        assertEquals(true, findIII("{123}").equals(
                new ArrayList<>(Arrays.asList("123"))));
        assertEquals(true, findIII("{{{444}}{}{}}").equals(
                new ArrayList<>(Arrays.asList("444"))));
        assertEquals(true, findIII("{{{222}}{}{{333}}}").equals(
                new ArrayList<>(Arrays.asList("222","333"))));
        assertEquals(true, findIII("{{{222}}{}{{{}333}}}").equals(
                new ArrayList<>(Arrays.asList(""))));
        assertEquals(true, findIII("{{{222}}{}{{{4}333}}}").equals(
                new ArrayList<>(Arrays.asList("4"))));
    }
}
