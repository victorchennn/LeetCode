package Companies.Google;

import java.util.Arrays;

public class PartitionEqualSubsetSum {
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int i : nums) {
            sum += i;
        }
        if (sum % 2 != 0) {
            return false;
        }
        Arrays.sort(nums);
        if (nums[nums.length-1] > sum/2) {
            return false;
        }
        return dfs(nums, sum/2, nums.length-1);
    }

    private boolean dfs(int[] nums, int target, int index) {
        if (target == 0) {
            return true;
        }
        for (int i = index; i >= 0; i--) {
            if (nums[i] <= target && dfs(nums, target-nums[i], i-1)) {
                return true;
            }
        }
        return false;
    }
}
