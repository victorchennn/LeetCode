package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * minimum number of steps to make two Strings Anagram
 */
public class AnagramsMinimumNumberofStepstoMakeTwoStrings {
    public static int minSteps(String s, String t) {
        Map<Character, Integer> m = new HashMap<>();
        for (char c : s.toCharArray()) {
            m.put(c, m.getOrDefault(c, 0)+1);
        }
        for (char c : t.toCharArray()) {
            if (m.containsKey(c) && m.get(c) > 0) {
                m.put(c, m.get(c)-1);
            }
        }
        int step = s.length()-t.length();
        for (char c : m.keySet()) {
            step += m.get(c);
        }
        return step;
    }

    @Test
    void test() {
        assertEquals(1, minSteps("ab", "bb"));
        assertEquals(-1, minSteps("abc", "abcb"));
        assertEquals(3, minSteps("kjm", "abc"));
        assertEquals(0, minSteps("wef", "few"));
    }
}
