package Companies.Amazon;

import Libs.TreeNode;

import java.util.Arrays;

public class HouseRobber {
    public int robI1(int[] nums) {
        int cur = 0, prev = 0;
        for (int num : nums) {
            int temp = cur;
            cur = Math.max(num+prev, cur);
            prev = temp;
        }
        return cur;
    }

    public int robI2(int[] nums) {
        int[] memo = new int[nums.length + 1];
        Arrays.fill(memo, -1);
        return helper(memo, nums, nums.length - 1);
    }

    private int helper(int[] memo, int[] nums, int i) {
        if (i < 0) {
            return 0;
        }
        if (memo[i] >= 0) {
            return memo[i];
        }
        int result = Math.max(helper(memo, nums, i - 2) + nums[i], helper(memo, nums, i - 1));
        memo[i] = result;
        return result;
    }

    /* Cycle */
    public int robII(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }
        return Math.max(helperII(nums, 0, nums.length-1), helperII(nums, 1, nums.length));
    }

    private int helperII(int[] nums, int l, int r) {
        int cur = 0, prev = 0;
        for (int i = l; i < r; i++) {
            int temp = cur;
            cur = Math.max(prev + nums[i], cur);
            prev = temp;
        }
        return cur;
    }

    /* TreeNode */
    public int rob(TreeNode root) {
        int[] dual = helper(root);
        return Math.max(dual[0], dual[1]);
    }

    private int[] helper(TreeNode root) {
        int[] re = new int[2];
        if (root == null) {
            return re;
        }
        int[] left = helper(root.left);
        int[] right = helper(root.right);
        re[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        re[1] = left[0] + right[0] + root.val;
        return re;
    }
}
