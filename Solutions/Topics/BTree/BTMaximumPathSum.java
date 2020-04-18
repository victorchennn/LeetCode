package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.List;

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
