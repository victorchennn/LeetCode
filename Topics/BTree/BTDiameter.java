package Topics.BTree;

import Libs.TreeNode;

public class BTDiameter {

    int max = 1;

    public int diameterOfBinaryTree(TreeNode root) {
        helper(root);
        return max-1;
    }

    private int helper(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = helper(root.left);
        int right = helper(root.right);
        max = Math.max(max, left+right+1);
        return Math.max(left, right)+1;
    }
}
