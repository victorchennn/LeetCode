package Topics.Design;

import java.util.Stack;

public class CustomStack {
    private int cap;
    private int[] increase;
    private Stack<Integer> stack;

    public CustomStack(int maxSize) {
        stack = new Stack<>();
        cap = maxSize;
        increase = new int[maxSize];
    }

    public void push(int x) {
        if (stack.size() < cap) {
            stack.push(x);
        }
    }

    public int pop() {
        int i = stack.size()-1;
        if (i < 0) {
            return -1;
        }
        if (i > 0) {
            increase[i-1] += increase[i];
        }
        int re = stack.pop()+increase[i];
        increase[i] = 0;
        return re;
    }

    public void increment(int k, int val) {
        int i = Math.min(k, stack.size())-1;
        if (i >= 0) {
            increase[i] += val;
        }
    }
}
