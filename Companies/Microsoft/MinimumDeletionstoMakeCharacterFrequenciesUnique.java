package Companies.Microsoft;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A string s is called good if there are no two different characters in s that have the same frequency.
 *
 * Given a string s, return the minimum number of characters you need to delete to make s good.
 */
public class MinimumDeletionstoMakeCharacterFrequenciesUnique {
    public int minDeletions(String s) {
        int[] count = new int[26];
        Set<Integer> unique = new HashSet<>();
        for (char c : s.toCharArray()) {
            count[c-'a']++;
        }
        int re = 0;
        for (int i = 0; i < count.length; i++) {
            while (count[i] > 0 && unique.contains(count[i])) {
                count[i]--;
                re++;
            }
            unique.add(count[i]);
        }
        return re;
    }

    @Test
    void test() {
        assertEquals(0, minDeletions("aab"));
        assertEquals(2, minDeletions("aaabbbcc"));
        assertEquals(2, minDeletions("ceabaacb"));
    }
}
