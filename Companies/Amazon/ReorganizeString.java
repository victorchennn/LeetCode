package Companies.Amazon;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Given a string s, rearrange the characters of s so that any two adjacent characters are not the same.
 *
 * Return any possible rearrangement of s or return "" if not possible.
 */
public class ReorganizeString {
    public String reorganizeString(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c-'a']++;
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>((a,b)->b[1]-a[1]);
        for (int i = 0; i < count.length; i++) {
            if (count[i] != 0) {
                pq.add(new int[]{i, count[i]});
            }
        }
        if (pq.peek()[1] > (s.length()+1)/2) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int[] prev = new int[2];
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            if (prev[1] > 0) {
                pq.add(prev);
            }
            sb.append((char)(cur[0]+'a'));
            cur[1]--;
            prev = cur;
        }
        return sb.toString();
    }

    @Test
    void test() {
        assertEquals("aba", reorganizeString("aab"));
        assertEquals("", reorganizeString("aaab"));
        assertEquals("vlvov", reorganizeString("vvvlo"));
    }
}
