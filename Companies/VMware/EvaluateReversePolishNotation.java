package Companies.VMware;

import java.util.Stack;

public class EvaluateReversePolishNotation {
    public int evalRPN(String[] tokens) {
        Stack<Integer> s = new Stack<>();
        for (String t : tokens) {
            if (!"+-*/".contains(t)) {
                s.push(Integer.valueOf(t));
                continue;
            }
            int num2 = s.pop();
            int num1 = s.pop();
            int re = 0;
            switch (t) {
                case "+":
                    re = num1 + num2;
                    break;
                case "-":
                    re = num1 - num2;
                    break;
                case "*":
                    re = num1 * num2;
                    break;
                case "/":
                    re = num1 / num2;
                    break;
            }
            s.push(re);
        }
        return s.pop();
    }
}
