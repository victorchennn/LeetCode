package Topics.BTree;

import Libs.TreeNode;

public class BTCountCompleteTreeNodes {
    public int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }
        TreeNode left = root, right = root;
        int height = 0;
        while (left != null && right != null) {
            left = left.left;
            right = right.right;
            height++;
        }
        if (left == null) {
            return (1 << height) -1;
        }
        return 1 + countNodes(root.left) + countNodes(root.right);
    }

    public int countNodes2(TreeNode root) {
        return root == null? 0 : 1 + countNodes2(root.left) + countNodes2(root.right);
    }
}
