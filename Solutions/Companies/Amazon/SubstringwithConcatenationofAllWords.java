package Companies.Amazon;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubstringwithConcatenationofAllWords {
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> re = new ArrayList<>();
        if (s == null || words == null || words.length == 0) {
            return re;
        }
        int sLen = s.length(), count = words.length, len = words[0].length();
        if (sLen < count*len) {
            return re;
        }
        Map<String, Integer> m = new HashMap<>();
        for (String word : words) {
            m.put(word, m.getOrDefault(word, 0)+1);
        }
        int limit = sLen - count*len + 1;
        for (int i = 0; i < len; i++) { // loop times
            for (int left = i; left < limit; left += len) {
                Map<String, Integer> visited = new HashMap<>();
                for (int index = count-1; index >= 0; index--) {
                    int pos = left + index*len;
                    String cur = s.substring(pos, pos+len);
                    int freq = visited.getOrDefault(cur, 0)+1;
                    if (freq > m.getOrDefault(cur, 0)) {
                        left = pos;
                        break;
                    }
                    visited.put(cur, freq);
                    if (index == 0) {
                        re.add(left);
                    }
                }
            }
        }
        return re;
    }

    @Test
    void test() {
        assertEquals(true, findSubstring("wordgoodgoodgoodbestword",
                new String[]{"word","good","best","word"}).equals(new ArrayList<>()));
        assertEquals(true, findSubstring("barfoothefoobarman",
                new String[]{"foo","bar"}).equals(new ArrayList<>(Arrays.asList(0, 9))));
    }
}
