package Topics.Binary;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * You're given the startTime, endTime and profit arrays, return the maximum profit
 * you can take such that there are no two jobs in the subset with overlapping time range.
 */
public class MaximumProfitinJobScheduling {
    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        List<int[]> vals = new ArrayList<>();
        for (int i = 0; i < startTime.length; i++) {
            vals.add(new int[]{startTime[i], endTime[i], profit[i]});
        }
        vals.sort((a,b)->a[1]-b[1]);
        int[] dp = new int[startTime.length];
        dp[0] = vals.get(0)[2];
        for (int i = 1; i < startTime.length; i++) {
            dp[i] = Math.max(dp[i-1], vals.get(i)[2]);
            int l = 0, r = i-1;
            while (l <= r) {
                int mid = l + (r-l)/2;
                if (vals.get(mid)[1] <= vals.get(i)[0]) {
                    l = mid+1;
                } else {
                    r = mid-1;
                }
            }
            if (r < 0) {
                continue;
            }
            dp[i] = Math.max(dp[i], dp[r] + vals.get(i)[2]);
        }
        return dp[startTime.length-1];
    }

    @Test
    void test() {
        assertEquals(120,
                jobScheduling(new int[]{1,2,3,3}, new int[]{3,4,5,6}, new int[]{50,10,40,70}));
        assertEquals(150,
                jobScheduling(new int[]{1,2,3,4,6}, new int[]{3,5,10,6,9}, new int[]{20,20,100,70,60}));
        assertEquals(6,
                jobScheduling(new int[]{1,1,1}, new int[]{2,3,4}, new int[]{5,6,4}));
    }
}
