package Companies.Bloomberg;

import java.util.LinkedList;
import java.util.Queue;

public class DecodeString {
    public String decodeString(String s) {
        Queue<Character> q = new LinkedList<>();
        for (char c : s.toCharArray()) {
            q.add(c);
        }
        return helper(q);
    }

    private String helper(Queue<Character> q) {
        StringBuilder sb = new StringBuilder();
        int num = 0;
        while (!q.isEmpty()) {
            char c = q.poll();
            if (Character.isDigit(c)) {
                num = num*10 + c-'0';
            } else if (c == '[') {
                String after = helper(q);
                for (int i = 0; i < num; i++) {
                    sb.append(after);
                }
                num = 0;
            } else if (c == ']') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private int i = 0;

    private String decodeStringII(String s) {
        StringBuilder sb = new StringBuilder();
        int num = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                num = num*10 + c-'0';
            } else if (c == '[') {
                i++;
                String after = decodeStringII(s);
                for (int k = 0; k < num; k++) {
                    sb.append(after);
                }
                num = 0;
            } else if (c == ']') {
                return sb.toString();
            } else {
                sb.append(c);
            }
            i++;
        }
        return sb.toString();
    }
}
