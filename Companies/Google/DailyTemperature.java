package Companies.Google;

import java.util.Stack;

public class DailyTemperature {
    public int[] dailyTemperatures(int[] T) {
        Stack<Integer> s = new Stack<>();
        int[] dp = new int[T.length];
        for (int i = T.length-1; i >= 0; i--) {
            while (!s.isEmpty() && T[i] >= T[s.peek()]) {
                s.pop();
            }
            dp[i] = s.isEmpty()?0:s.peek()-i;
            s.push(i);
        }
        return dp;
    }
}
