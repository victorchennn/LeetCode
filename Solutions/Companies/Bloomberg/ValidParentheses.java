package Companies.Bloomberg;

import java.util.Stack;

/**
 * Follow-up:
 * What if we add single quotations marks?
 *
 * Input: {'}' -> Output: false
 * Explanation: Because we only have 1 quotation after bracket.
 *
 * I guess if we consider ' as another type of closing bracket, everything else is same.
 */
public class ValidParentheses {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(')');
            } else if (c == '[') {
                stack.push(']');
            } else if (c == '{') {
                stack.push('}');
            } else {
                if (stack.isEmpty() || stack.pop() != c) {
                    return false;
                }
            }
        }
        return stack.isEmpty(); // !!!
    }
}
