package Companies.Google;

import java.util.LinkedList;
import java.util.Queue;

public class BasicCalculatorIII {
    public int calculate(String s) {
        Queue<Character> q = new LinkedList<>();
        for (char c : s.toCharArray()) {
            if (c != ' ') {
                q.offer(c);
            }
        }
        q.add('+');  // Be careful
        return helper(q);
    }

    private int helper(Queue<Character> q) {
        int sum = 0, prev = 0, num = 0;
        char sign = '+'; // last sign
        while (!q.isEmpty()) {
            char c = q.poll();
            if (Character.isDigit(c)) {
                num = num * 10 + c - '0';
            } else if (c == '(') {
                num = helper(q);
            } else {
                if (sign == '+') {  // check last sign
                    sum += prev;
                    prev = num;
                } else if (sign == '-') {
                    sum += prev;
                    prev = -num;
                } else if (sign == '*') {
                    prev *= num;
                } else {
                    prev /= num;
                }
                if (c == ')') {
                    break;
                }
                sign = c;       // update last sign
                num = 0;
            }
        }
        return sum + prev;
    }
}
