package Companies.Microsoft;

import java.util.Arrays;

public class LongestIncreasingSubsequence {
    /* O(NlogN) */
    public int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        int len = 0;
        for (int num : nums) {
            int i = Arrays.binarySearch(dp, 0, len, num);
            if (i < 0) {
                i = -(i+1);
            }
            dp[i] = num;
            if (i == len) {
                len++;
            }
        }
        return len;
    }

    private int helper(int[] nums, int l, int r, int num) {
        while (l < r) {
            int mid = (l+r)/2;
            if (nums[mid] >= num) {
                r = mid;
            } else {
                l = mid+1;
            }
        }
        return l;
    }

    /* O(N^2) */
    public int lengthOfLIS_II(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int[] len = new int[nums.length];
        len[0] = 1;
        int re = 1;
        for (int i = 1; i < nums.length; i++) {
            int temp = 1;
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    temp = Math.max(temp, len[j]+1);
                }
            }
            len[i] = temp;
            re = Math.max(re, temp);
        }
        return re;
    }
}
