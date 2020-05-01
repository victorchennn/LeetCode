package Companies.Bloomberg;

import java.util.Deque;
import java.util.LinkedList;

public class TernaryExpressionParser {
    public String parseTernary(String expression) {
        if (expression == null || expression.length() == 0) {
            return "";
        }
        Deque<Character> stack = new LinkedList<>();
        for (int i = expression.length()-1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (!stack.isEmpty() && stack.peek() == '?') {
                stack.pop(); // pop '?'
                char first = stack.pop();
                stack.pop(); // pop ':'
                char second = stack.pop();

                if (c == 'T') {
                    stack.push(first);
                } else {
                    stack.push(second);
                }
            } else {
                stack.push(c);
            }
        }
        return String.valueOf(stack.peek());

//        return Character.toString(helper(expression));
    }

    private int index = 0;

    private char helper(String s) {
        char ch = s.charAt(index);
        if (index + 1 == s.length() || s.charAt(index + 1) == ':') {
            index += 2; // pass ':'
            return ch;
        }
        index += 2; // pass '?'
        char first = helper(s), second = helper(s);
        return ch == 'T' ? first : second;
    }
}
