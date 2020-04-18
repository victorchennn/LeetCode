package Companies.Microsoft;

import java.util.Stack;

public class DailyTemperatures {
    public int[] dailyTemperatures(int[] T) {
        Stack<Integer> s = new Stack<>();
        int[] re = new int[T.length];

        for (int i = 0; i < T.length; i++) {
            while (!s.isEmpty() && T[s.peek()] < T[i]) {
                re[s.peek()] = i - s.pop();
            }
            s.push(i);
        }
        return re;
    }
}
