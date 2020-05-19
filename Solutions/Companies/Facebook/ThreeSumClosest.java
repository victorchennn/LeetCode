package Companies.Facebook;

import java.util.Arrays;

public class ThreeSumClosest {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int re = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length-2; i++) {
            if (i != 0 && nums[i] == nums[i-1]) {
                continue;
            }
            int l = i+1, r = nums.length-1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (Math.abs(target-sum) < Math.abs(target-re)) {
                    re = sum;
                }
                if (target == sum) {
                    return sum;
                } else if (sum < target) {
                    l++;
                } else {
                    r--;
                }
            }
        }
        return re;
    }
}
