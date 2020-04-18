package Companies.Google;

public class PartitionToKEqualSumSubsets {
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0;
        for (int i : nums) {
            sum += i;
        }
        if (sum % k != 0) {
            return false;
        }
        return dfs(nums, k, sum/k, 0, 0, new boolean[nums.length]);
    }

    private boolean dfs(int[] nums, int k, int target, int sum, int start, boolean[] marks) {
        if (k == 1) {
            return true;
        }
        if (sum == target) {
            return dfs(nums, k-1, target, 0, 0, marks);
        }
        if (sum > target) {
            return false;
        }
        for (int i = start; i < nums.length; i++) {
            if (!marks[i]) {
                marks[i] = true;
                if (dfs(nums, k, target, sum+nums[i], i+1, marks)) {
                    return true;
                }
                marks[i] = false;
            }
        }
        return false;
    }
}
