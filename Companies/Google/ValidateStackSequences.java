package Companies.Google;

import java.util.Stack;

public class ValidateStackSequences {
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        Stack<Integer> s = new Stack<>();
        int index = 0;
        for (int i : pushed) {
            s.push(i);
            while (!s.isEmpty() && s.peek() == popped[index]) {
                s.pop();
                index++;
            }
        }
        return s.isEmpty();
    }
}
