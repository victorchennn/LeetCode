package Companies.Google;

import java.util.Arrays;

public class MinimumDifferenceBetweenLargestandSmallestValueinThreeMoves {
    public int minDifference(int[] nums) {
        int n = nums.length, res = Integer.MAX_VALUE;
        if (n < 5) return 0;
        Arrays.sort(nums);
        for (int i = 0; i < 4; ++i) {
            res = Math.min(res, nums[n - 4 + i] - nums[i]);
        }
        return res;
    }
}
