package Companies.Google;

import java.util.Stack;

public class BasicCalculatorII {
    public int calculate(String s) {
        s = s.replace(" ", "");
        Stack<Integer> stack = new Stack<>();
        char sign = '+';
        int num = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                num = num*10 + c - '0';
            }
            if (!Character.isDigit(c) || i == s.length()-1) {
                if (sign == '+') {
                    stack.push(num);
                } else if (sign == '-') {
                    stack.push(-num);
                } else if (sign == '*') {
                    stack.push(stack.pop()*num);
                } else {
                    stack.push(stack.pop()/num);
                }
                sign = c;
                num = 0;
            }
        }
        int re = 0;
        for (int i : stack) {
            re += i;
        }
        return re;
    }
}
