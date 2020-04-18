package Companies.Google;

import Libs.TreeNode;

public class ValidateBST {
    public boolean isValidBST(TreeNode root) {
//        Stack<TreeNode> s = new Stack<>();
//        double low = -Double.MAX_VALUE;
//        while (root != null || !s.isEmpty()) {
//            while (root != null) {
//                s.push(root);
//                root = root.left;
//            }
//            root = s.pop();
//            if (root.val <= low) {
//                return false;
//            }
//            low = root.val;
//            root = root.right;
//        }
//        return true;

        return check(root, null, null);
    }

    private boolean check(TreeNode root, Integer low, Integer high) {
        if (root == null) {
            return true;
        }
        if (low != null && low >= root.val) return false;
        if (high != null && high <= root.val) return false;
        if (!check(root.left, low, root.val)) return false;
        if (!check(root.right, root.val, high)) return false;
        return true;
    }
}
