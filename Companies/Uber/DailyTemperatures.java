package Companies.Uber;

import java.util.Stack;

public class DailyTemperatures {
    /**
     * Next greater position
     * O(NW), N is length of array, W is the number of allowed values for T[i].
     * W = 71, we can consider this complexity O(N).
     */
    public int[] dailyTemperatures(int[] T) {
        int[] re = new int[T.length];
        for (int i = T.length-2; i >= 0; i--) {
            int next = i+1;
            while (next < T.length && T[i] >= T[next]) {
                if (re[next] == 0) {
                    next = T.length;
                } else {
                    next += re[next];
                }
            }
            if (next < T.length) {
                re[i] = next-i;
            }
        }
        return re;
    }

    public int[] dailyTemperaturesII(int[] T) {
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
