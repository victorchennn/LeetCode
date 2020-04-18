package Companies.Google;

import java.util.Stack;

public class MinStack {

    int min;
    Stack<Integer> s;

    /** initialize your data structure here. */
    public MinStack() {
        s = new Stack<>();
        min = Integer.MAX_VALUE;
    }

    public void push(int x) {
        if (x <= min) {
            s.push(min);
            min = x;
        }
        s.push(x);
    }

    public void pop() {
        if (min == s.pop()) {
            min = s.pop();
        }
    }

    public int top() {
        return s.peek();
    }

    public int getMin() {
        return min;
    }
}
