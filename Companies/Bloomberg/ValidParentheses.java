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

    /**
     * Follow up.
     * Using 'X' to replace any of the bracket.
     */
    private static boolean isBalanced(String s, Stack<Character> stack, int index) {
        if (index == s.length()) {
            return stack.size() == 0;
        }
        char cur = s.charAt(index);
        if (cur == '{' || cur == '(' || cur == '[') {
            stack.push(cur);
            return isBalanced(s, stack, index+1);
        } else if (cur == '}' || cur == ')' || cur == ']') {
            if (stack.size() == 0) {
                return false;
            }
            char top = stack.pop(); // should be matching bracket or 'X'
            if (!isMatching(top, cur)) {
                return false;
            }
            return isBalanced(s, stack, index+1);
        } else if (cur == 'X') {
            Stack<Character> temp = new Stack<>();
            temp.addAll(stack);
            temp.push(cur);
            if (isBalanced(s, temp, index+1)) {
                return true;
            }
            if (stack.size() == 0) {
                return false;
            }
            stack.pop();
            return isBalanced(s, stack, index+1);
        }
        return true;
    }

    private static boolean isMatching(char a, char b) {
        if ((a == '{' && b == '}')
                || (a == '[' && b == ']')
                || (a == '(' && b == ')') || a == 'X') {
            return true;
        }
        return false;
    }

    public static void main(String...args) {
        System.out.println(isBalanced("{(X[X])}", new Stack<>(), 0));
        System.out.println(isBalanced("[{X}(X)]", new Stack<>(), 0));
    }
}
