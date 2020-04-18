package Companies.Google;

import Libs.TreeNode;

public class SplitBST {
    public TreeNode[] splitBST(TreeNode root, int V) {
        if (root == null) {
            return new TreeNode[]{null, null};
        } else if (root.val <= V) {
            TreeNode[] right = splitBST(root.right, V);
            root.right = right[0];
            right[0] = root;
            return right;
        } else {
            TreeNode[] left = splitBST(root.left, V);
            root.left = left[1];
            left[1] = root;
            return left;
        }
    }
}
