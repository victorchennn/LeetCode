package Companies.Microsoft;

import java.util.PriorityQueue;

public class ReorganizeString {
    public String reorganizeString(String S) {
        int[] count = new int[26];
        for (char c : S.toCharArray()) {
            count[c-'a']++;
        }
        PriorityQueue<int[]> q = new PriorityQueue<>((a, b)->b[1]-a[1]);
        for (int i = 0; i < 26; i++) {
            if (count[i] != 0) {
                q.add(new int[]{i, count[i]});
            }
        }
        if (q.peek()[1] > (S.length()+1)/2) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int[] prev = new int[]{-1, 0};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            if (prev[1] > 0) {
                q.add(prev);
            }
            sb.append((char)(cur[0]+'a'));
            cur[1]--;
            prev = cur;
        }
        return sb.toString();
    }
}
