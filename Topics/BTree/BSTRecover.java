package Topics.BTree;

import Libs.TreeNode;

public class BSTRecover {
    private TreeNode x = null, y = null, pre = null;

    public void recoverTree(TreeNode root) {
        helper(root);

//        Stack<TreeNode> s = new Stack<>();
//        while (!s.isEmpty() || root != null) {
//            while (root != null) {
//                s.push(root);
//                root = root.left;
//            }
//            root = s.pop();
//            if (pre != null && pre.val > root.val) {
//                y = root;
//                if (x == null) {
//                    x = pre;
//                } else {
//                    break;
//                }
//            }
//            pre = root;
//            root = root.right;
//        }

        int temp = x.val;
        x.val = y.val;
        y.val = temp;
    }

    private void helper(TreeNode root) {
        if (root != null) {
            helper(root.left);
            if (pre != null && pre.val > root.val) {
                y = root;
                if (x == null) {
                    x = pre;
                } else {
                    return;
                }
            }
            pre = root;
            helper(root.right);
        }
    }
}
