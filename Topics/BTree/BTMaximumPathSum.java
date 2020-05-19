package Topics.BTree;

import Libs.TreeNode;

/**
 * Time complexity: O(N), where N is number of nodes, since we visit each node not more than 2 times.
 * Space complexity: O(log(N)), we have to keep a recursion stack of the size of the tree height.
 */
public class BTMaximumPathSum {
    int max = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        helper(root);
        return max;
    }

    private int helper(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = Math.max(0, helper(root.left));
        int right = Math.max(0, helper(root.right));
        max = Math.max(max, left+right+root.val);
        return root.val + Math.max(left, right);
    }
}
