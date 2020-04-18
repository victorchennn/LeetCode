package Topics.BTree;

import Libs.TreeNode;

public class BSTFromSortedArray {
    public TreeNode sortedArrayToBST(int[] nums) {
        if (nums.length == 0) {
            return null;
        }
        return helper(nums, 0, nums.length-1);
    }

    private TreeNode helper(int[] nums, int l, int r) {
        if (l > r) {
            return null;
        }
        int m = l + (r-l)/2;
        // if ((l+r)%2 == 1) m++                 // choose right Middle Node as Root
        // if ((l+r)%2 == 1) m+=rand.nextInt(2)  // choose random Middle Node as Root

        TreeNode root = new TreeNode(nums[m]);
        root.left = helper(nums, l, m-1);
        root.right = helper(nums, m+1, r);
        return root;
    }
}
