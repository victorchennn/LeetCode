package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see SortCharByFrequency
 */
public class FindMostFrequentCharinStr {
    public char find(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c-'a']++;
        }
        int max = 0;
        char c = s.charAt(0);
        for (int i = 0; i < count.length; i++) {
            if (count[i] > max) {
                max = count[i];
                c = (char)('a'+i);
            }
        }
        return c;
    }

    @Test
    void test() {
        assertEquals('b', find("bloomberg"));
        assertEquals('o', find("blooomberg"));
    }
}
