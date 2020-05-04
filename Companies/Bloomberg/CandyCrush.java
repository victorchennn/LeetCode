package Companies.Bloomberg;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see RemoveAllAdjacentDuplicatesInString
 */
public class CandyCrush {
    public static String candyCrush(String s) {
        Deque<Pair<Character, Integer>> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int count = 1;
            while (i < s.length()-1 && s.charAt(i+1) == c) {
                count++;
                i++;
            }
            if (!stack.isEmpty() && stack.peek().getKey() == c) {
                count += stack.pop().getValue();
            }
            if (count < 3) {
                stack.push(new Pair<>(c, count));
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            Pair<Character, Integer> l = stack.pop();
            for (int i = l.getValue(); i > 0; i--) {
                sb.insert(0, l.getKey());
            }
        }
        return sb.toString();
    }

    /**
     * @Follow-up: ask for shortest string
     */
    public static String candyCrushII(String s) {
        String forward = helper(s, false);
        String backward = helper(s, true);
        if (forward.equals(s) && backward.equals(s)) {
            return s;
        }
        String re1 = candyCrushII(forward);
        String re2 = candyCrushII(backward);
        return re1.length() < re2.length()? re1:re2;
     }


    private static String helper(String s, boolean reverse) {
        if (!reverse) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                int start = i;
                int count = 1;
                while (i < s.length()-1 && s.charAt(i+1) == c) {
                    count++;
                    i++;
                }
                if (count >= 3) {
                    return s.substring(0, start) + s.substring(i+1);
                }
            }
        } else {
            for (int i = s.length()-1; i >= 0; i--) {
                char c = s.charAt(i);
                int start = i;
                int count = 1;
                while (i > 0 && s.charAt(i-1) == c) {
                    count++;
                    i--;
                }
                if (count >= 3) {
                    return s.substring(0, i) + s.substring(start+1);
                }
            }
        }
        return s;
    }

    @Test
    void test() {
        assertEquals("a", candyCrush("acaaaacc"));
        assertEquals("c", candyCrush("aaabbbc"));
        assertEquals("cd", candyCrush("aabbbacd"));
        assertEquals("", candyCrush("aabbccddeeedcba"));

        assertEquals("aaaacaaabbba", helper("abbbaaacaaabbba", false));
        assertEquals("abbbaaacaaaa", helper("abbbaaacaaabbba", true));

        assertEquals("acd", candyCrush("aaabbbacd"));
        assertEquals("cd", candyCrushII("aaabbbacd"));
        assertEquals("ca", candyCrush("abbbaaacaaabbba"));
        assertEquals("c", candyCrushII("abbbaaacaaabbba"));
    }
}
