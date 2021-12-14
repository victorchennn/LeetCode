package Topics.BTree;

import Libs.TreeNode;

/**
 * root.val = min(root.left.val, root.right.val)
 */
public class BTSecondMinimumNode {
    public int findSecondMinimumValue(TreeNode root) {
        return dfs(root, root.val);
    }

    private int dfs(TreeNode root, int val) {
        if (root == null) {
            return -1;
        }
        if (root.val > val) {
            return root.val;
        }
        int left = dfs(root.left, val);
        int right = dfs(root.right, val);
        return (left != -1 && right != -1)? Math.min(left, right) : Math.max(left, right);
    }
}
