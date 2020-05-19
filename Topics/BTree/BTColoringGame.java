package Topics.BTree;

import Libs.TreeNode;

public class BTColoringGame {
    int left = 0, right = 0;

    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {
        count(root, x);
        return Math.max(Math.max(left, right), n-left-right-1) > n/2;
    }

    private int count(TreeNode root, int val) {
        if (root == null) {
            return 0;
        }
        int l = count(root.left, val);
        int r = count(root.right, val);
        if (root.val == val) {
            left = l;
            right = r;
        }
        return l+r+1;
    }
}
