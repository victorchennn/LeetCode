package Topics.BTree;

import Libs.TreeNode;

public class BTCountUnivalueSubtrees {
    private int count = 0;

    public int countUnivalSubtrees(TreeNode root) {
//        helper(root);

        helper(root, 0);
        return count;
    }

    private boolean helper(TreeNode root, int val) {
        if (root == null) {
            return true;
        }
        if (!helper(root.left, root.val) | !helper(root.right, root.val)) {
            return false;
        }
        count++;
        return root.val == val;
    }


    private boolean helper(TreeNode root) {
        if (root.left == null && root.right == null) {
            count++;
            return true;
        }
        boolean isUni = true;
        if (root.left != null) {
            isUni = helper(root.left) && root.left.val == root.val;
        }
        if (root.right != null) {
            isUni = helper(root.right) && root.right.val == root.val && isUni; // be careful of last {isUni}
        }
        if (!isUni) {
            return false;
        }
        count++;
        return true;
    }
}
