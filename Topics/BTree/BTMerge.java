package Topics.BTree;

import Libs.TreeNode;

public class BTMerge {
    /**
     * m is the minimum number of nodes from the two given trees.
     * Time Complexity: O(m)
     * Space Complexity: O(m), average O(logm)
     */
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1 == null || t2 == null) {
            return t1 == null?t2:t1;
        }
        TreeNode root = new TreeNode(t1.val+t2.val);
        root.left = mergeTrees(t1.left, t2.left);
        root.right = mergeTrees(t1.right, t2.right);
        return root;
    }
}
