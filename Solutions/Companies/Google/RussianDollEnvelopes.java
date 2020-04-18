package Companies.Google;

import java.util.Arrays;

public class RussianDollEnvelopes {
    public int maxEnvelopes(int[][] envelopes) {
        Arrays.sort(envelopes, (o1, o2) -> {
            if (o1[0] == o2[0]) {
                return o2[1] - o1[1];
            }
            return o1[0] - o2[0];
        });
        int[] dp = new int[envelopes.length];
        int len = 0;
        for (int[] e : envelopes) {
            int index = Arrays.binarySearch(dp, 0, len, e[1]);
            if (index < 0) {
                index = -(index+1);
            }
            dp[index] = e[1];
            if (index == len) {
                len++;
            }
        }
        return len;
    }
}
