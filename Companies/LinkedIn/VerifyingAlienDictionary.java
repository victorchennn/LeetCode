package Companies.LinkedIn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerifyingAlienDictionary {
    public boolean isAlienSorted(String[] words, String order) {
        int[] orders = new int[26];
        for (int i = 0; i < order.length(); i++) {
            orders[order.charAt(i)-'a'] = i;
        }
        for (int i = 0; i < words.length-1; i++) {
            if (compare(words[i], words[i+1], orders) > 0) {
                return false;
            }
        }
        return true;
    }

    private int compare(String s1, String s2, int[] orders) {
        int len1 = s1.length(), len2 = s2.length();
        for (int i = 0; i < Math.min(len1, len2); i++) {
            int o1 = s1.charAt(i)-'a';
            int o2 = s2.charAt(i)-'a';
            if (o1 != o2) {
                return orders[o1] - orders[o2];
            }
        }
        return len1 <= len2? -1:1;
    }

    @Test
    void test() {
        assertEquals(true, isAlienSorted(new String[]{"hello","leetcode"}, "hlabcdefgijkmnopqrstuvwxyz"));
        assertEquals(false, isAlienSorted(new String[]{"word","world","row"}, "worldabcefghijkmnpqstuvxyz"));
        assertEquals(false, isAlienSorted(new String[]{"apple","app"}, "abcdefghijklmnopqrstuvwxyz"));
    }
}
