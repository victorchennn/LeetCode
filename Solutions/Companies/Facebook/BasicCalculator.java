package Companies.Facebook;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The expression string may contain open '(' and closing parentheses ')',
 * plus '+' or minus '-', non-negative integers and empty spaces .
 */
public class BasicCalculator {
    public int calculate(String s) {
        Queue<Character> q = new LinkedList<>();
        for (char c : s.toCharArray()) {
            if (c != ' ') {
                q.add(c);
            }
        }
        q.add('+');
        return helper(q);
    }

    private int helper(Queue<Character> q) {
        int sum = 0, prev = 0, num = 0;
        char sign = '+';
        while (!q.isEmpty()) {
            char c = q.poll();
            if (Character.isDigit(c)) {
                num = num*10+c-'0';
            } else if (c == '(') {
                num = helper(q);
            } else {
                if (sign == '+') {
                    sum += prev;
                    prev = num;
                } else {
                    sum += prev;
                    prev = -num;
                }
                if (c == ')') {
                    break;
                }
                sign = c;
                num = 0;
            }
        }
        return sum+prev;
    }
}
