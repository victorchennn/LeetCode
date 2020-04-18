package Topics.BTree;

import Libs.TreeNode;

public class BSTClosestValue {
    public int closestValue(TreeNode root, double target) {
        int re = root.val;
        while (root != null) {
            if (Math.abs(target-root.val) < Math.abs(target-re)) {
                re = root.val;
            }
            root = root.val > target?root.left:root.right;
        }
        return re;
    }
}
