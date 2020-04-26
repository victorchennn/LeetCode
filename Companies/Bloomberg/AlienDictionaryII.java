package Companies.Bloomberg;

import java.util.*;

/**
 * Dic: "a","b","c","d","ch","dd","e","r"
 * words: "ddr","dea","ab","chc"
 *
 * Sort: "ab","dea","chc","ddr"
 */
public class AlienDictionaryII {
    public static List<String> sort(List<String> l, List<String> words) {
        Map<String, Integer> m = new HashMap<>();
        for (int i = 1; i <= l.size(); i++) {
            m.put(l.get(i-1), i);
        }
        words.sort((a, b) -> compare(a, b, m));
        return words;
    }

    /* Sorting based on dictionary maximal match */
    private static int compare(String a, String b, Map<String, Integer> m) {
        if (a.equals(b)) {
            return 0;
        }
        if (a.length() == 0 || b.length() == 0) {
            return a.length() == 0? -1:1;
        }
        int i = 2, j = 2, num1 = 0, num2 = 0;
        while (i >= 1) {
            if (i <= a.length() && m.containsKey(a.substring(0, i))) {
                num1 = m.get(a.substring(0, i));
                break;
            }
            i--;
        }
        while (j >= 1) {
            if (j <= b.length() && m.containsKey(b.substring(0, j))) {
                num2 = m.get(b.substring(0, j));
                break;
            }
            j--;
        }
        if (num1 != num2) {
            return num1-num2;
        }
        return compare(a.substring(i), b.substring(j), m);
    }

    public static void main(String...args) {
        List<String> dic = new ArrayList<>(Arrays.asList("a","b","c","d","ch","dd","e","r"));
        List<String> l = new ArrayList<>(Arrays.asList("ddr", "dea", "ab", "chc", "a"));
        sort(dic, l);
    }
}
