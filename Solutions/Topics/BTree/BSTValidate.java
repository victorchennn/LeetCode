package Topics.BTree;

import Libs.TreeNode;

import java.util.Stack;

public class BSTValidate {
    public boolean isValidBST(TreeNode root) {
        Stack<TreeNode> s = new Stack<>();
        double min = -Double.MAX_VALUE;  // ！！！
        while (!s.isEmpty() || root != null) {
            while (root != null) {
                s.push(root);
                root = root.left;
            }
            root = s.pop();
            if (root.val <= min) {
                return false;
            }
            min = root.val;
            root = root.right;
        }
        return true;

        // return helper(root, null, null);
    }

    private boolean helper(TreeNode root, Integer min, Integer max) {
        if (root == null) {
            return true;
        }
        if ((min != null && root.val <= min) || (max != null && root.val >= max)) {
            return false;
        }
        return helper(root.left, min, root.val) && helper(root.right, root.val, max);
    }
}
