package Companies.LinkedIn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * return true if it is possible to divide this array into k non-empty subsets whose sums are all equal.
 */
public class PartitionToKEqualSumSubsets {
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % k != 0) {
            return false;
        }
        return dfs(nums, k, sum/k, 0, 0, new boolean[nums.length]);
    }

    private boolean dfs(int[] nums, int k, int target, int sum, int start, boolean[] marked) {
        if (k == 1) {
            return true;
        }
        if (sum == target) {
            return dfs(nums, k-1, target, 0, 0, marked);
        }
        if (sum > target) {
            return false;
        }
        for (int i = start; i < nums.length; i++) {
            if (!marked[i]) {
                marked[i] = true;
                if (dfs(nums, k, target, sum+nums[i], i+1, marked)) {
                    return true;
                }
                marked[i] = false;
            }
        }
        return false;
    }

    @Test
    void test() {
        assertEquals(true, canPartitionKSubsets(new int[]{4,3,2,3,5,2,1},4));
        assertEquals(false, canPartitionKSubsets(new int[]{1,2,3,4},3));
    }
}
