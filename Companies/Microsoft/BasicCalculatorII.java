package Companies.Microsoft;

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
                num = num*10 + c-'0';
            }
            if (!Character.isDigit(c) || i == s.length()-1) {
                switch(sign) {
                    case '+':
                        stack.push(num);
                        break;
                    case'-':
                        stack.push(-num);
                        break;
                    case'*':
                        stack.push(stack.pop()*num);
                        break;
                    case'/':
                        stack.push(stack.pop()/num);
                        break;
                }
                num = 0;
                sign = c;
            }
        }
        int re = 0;
        for (int i : stack) {
            re += i;
        }
        return re;
    }
}
