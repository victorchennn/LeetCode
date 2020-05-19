package Topics.BTree;

import Libs.TreeNode;

public class BTFlattentoLinkedList {
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode node = root;
        while (node != null) {
            if (node.left != null) {
                TreeNode rightmost = node.left;
                while (rightmost.right != null) {
                    rightmost = rightmost.right;
                }
                rightmost.right = node.right;
                node.right = node.left;
                node.left = null;
            }
            node = node.right;
        }

//        helper(root, null);
    }

    private TreeNode helper(TreeNode root, TreeNode pre) {
        if (root == null) {
            return pre; // be careful
        }
        pre = helper(root.right, pre);
        pre = helper(root.left, pre);
        root.right = pre;
        root.left = null;
        return root;
    }
}
