package Companies.Google;

import java.util.PriorityQueue;
import java.util.Stack;

public class ReorganizeString {
    public String reorganizeString(String S) {
        int[] dict = new int[26];
        for (char c : S.toCharArray()) {
            dict[c-'A']++;
        }
        PriorityQueue<int[]> q = new PriorityQueue<>((a, b)->b[1]-a[1]);
        for (int i = 0; i < 26; i++) {
            if (dict[i] != 0) {
                q.offer(new int[]{i, dict[i]});
            }
        }
        if (q.peek()[1] > (S.length()+1)/2) { // aaab
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int[] prev = {-1, 0}; // vvvlo
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            if (prev[1] > 0) {
                q.offer(prev);
            }
            sb.append((char)(cur[0]+'A'));  // be careful (char)
            cur[1]--;
            prev = cur;
        }
        return sb.toString();
    }

    public static void main(String...args) {
//        Companies.Google.ReorganizeString test = new Companies.Google.ReorganizeString();
//        System.out.println(test.reorganizeString("aaab"));
        Stack<String> stack = new Stack<>();
        stack.push("");
        stack.push("b");
        for (String s : stack) {
            System.out.println(s);
        }

    }
}
