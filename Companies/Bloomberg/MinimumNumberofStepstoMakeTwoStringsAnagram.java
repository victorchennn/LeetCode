package Companies.Bloomberg;

import java.util.HashMap;
import java.util.Map;

public class MinimumNumberofStepstoMakeTwoStringsAnagram {
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

    public static void main(String...args) {
        System.out.println(minSteps("ab", "bb")); // 1
        System.out.println(minSteps("abc", "abcb")); // -1
        System.out.println(minSteps("kjm", "abc")); // 3
        System.out.println(minSteps("wef", "few")); // 0
    }
}
