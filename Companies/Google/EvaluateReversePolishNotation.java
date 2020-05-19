package Companies.Google;

import java.util.Stack;

public class EvaluateReversePolishNotation {
    public int evalRPN(String[] tokens) {
        Stack<Integer> s = new Stack<>();
        for (String t : tokens) {
            if (t.equals("+")) {
                s.push(s.pop() + s.pop());
            } else if (t.equals("-")) {
                int l1 = s.pop();
                int l2 = s.pop();
                s.push(l2-l1);
            } else if (t.equals("*")) {
                s.push(s.pop() * s.pop());
            } else if (t.equals("/")) {
                int l1 = s.pop();
                int l2 = s.pop();
                s.push(l2/l1);
            } else {
                s.push(Integer.valueOf(t));
            }
        }
        return s.pop();
    }

}
