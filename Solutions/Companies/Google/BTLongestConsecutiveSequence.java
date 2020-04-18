package Companies.Google;

import Libs.TreeNode;

public class BTLongestConsecutiveSequence {

    public int longestConsecutive(TreeNode root) {
        return find(root, null, 0);
    }

    private int find(TreeNode root, TreeNode parent, int len) {
        if (root == null) {
            return len;
        }
        len = (parent != null && root.val == parent.val+1)?len+1:1;
        return Math.max(len, Math.max(find(root.left, root, len), find(root.right, root, len)));
    }
}
