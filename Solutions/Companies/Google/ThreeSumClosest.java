package Companies.Google;

import java.util.Arrays;

public class ThreeSumClosest {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int re = nums[0]+nums[1]+nums[2];
        for (int i = 0; i < nums.length; i++) {
            int l = i+1, r = nums.length-1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (Math.abs(sum-target) < Math.abs(re-target)) {
                    re = sum;
                }
                if (sum < target) {
                    l++;
                } else {
                    r++;
                }
            }
        }
        return re;
    }
}
