package Topics.Design;

import java.util.ArrayDeque;
import java.util.Deque;

public class MaxStack {
    private Deque<Integer> stack;
    private Deque<Integer> maxStack;

    /** initialize your data structure here. */
    public MaxStack() {
        stack = new ArrayDeque<>();
        maxStack = new ArrayDeque<>();
    }

    public void push(int x) {
        stack.push(x);
        if(maxStack.isEmpty() || x>=maxStack.peek()) {
            maxStack.push(x);
        } else {
            maxStack.push(maxStack.peek());
        }
    }

    public int pop() {
        maxStack.pop();
        return stack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int peekMax() {
        return maxStack.peek();
    }

    public int popMax() {
        int max = peekMax();

        Deque<Integer> temp = new ArrayDeque<>();
        while(top() != max) {
            temp.push(pop());
        }
        pop();

        while(!temp.isEmpty())
            push(temp.pop());

        return max;
    }
}
