package Topics.BTree;

import Libs.TreeNode;

public class BTBalanced {
    public boolean isBalanced(TreeNode root) {
        return getHeight(root) != -1;
    }

    private int getHeight(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int lh = getHeight(root.left);
        int rh = getHeight(root.right);

        if (lh == -1 || rh == -1 || Math.abs(lh-rh) > 1) {
            return -1;
        }
        return Math.max(lh, rh)+1;
    }
}
