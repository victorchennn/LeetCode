package Topics.Design;

import java.util.Stack;

/**
 * Time Complexity: O(1)
 * Space Complexity: O(N)
 * Single stack need O(2*N) space, add a minstack can help reduce the space, but if
 * there are multiple same min existing, can push freq also as int[] pair to minstack.
 *
 * If need popMin(), see @MaxStack use treemap to make a balance.
 */
public class MinStack {
    private Stack<int[]> s = new Stack<>();

//    private Stack<Integer> stack = new Stack<>();
//    private Stack<Integer> minStack = new Stack<>();


//    private Stack<Integer> stack = new Stack<>();
//    private Stack<int[]> minStack = new Stack<>();  // int[min, freq]

    public MinStack() {}

    public void push(int x) {
        if (s.isEmpty()) {
            s.push(new int[]{x, x});
        } else {
            s.push(new int[]{x, Math.min(s.peek()[1], x)});
        }

        /* Only push minimum */
//        stack.push(x);
//        if (minStack.isEmpty() || x <= minStack.peek()) {
//            minStack.push(x);
//        }

        /* Only push non-exist minimum */
//        stack.push(x);
//        if (minStack.isEmpty() || x < minStack.peek()[0]) {
//            minStack.push(new int[]{x, 1});
//        } else if (x == minStack.peek()[0]) {
//            minStack.peek()[1]++;
//        }
    }

    public void pop() {
        s.pop();

//        if (stack.peek().equals(minStack.peek())) {
//            minStack.pop();
//        }
//        stack.pop();

        /* Only pop freq==0 minimum */
//        if (stack.peek().equals(minStack.peek()[0])) {
//            minStack.peek()[1]--;
//        }
//        if (minStack.peek()[1] == 0) {
//            minStack.pop();
//        }
//        stack.pop();
    }

    public int top() {
        return s.peek()[0];

//        return stack.peek();

//        return stack.peek();
    }

    public int getMin() {
        return s.peek()[1];

//        return minStack.peek();

//        return minStack.peek()[0];
    }

}
