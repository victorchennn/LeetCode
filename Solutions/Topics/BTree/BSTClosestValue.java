package Topics.BTree;

import Libs.TreeNode;

import java.util.LinkedList;
import java.util.List;

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

    public List<Integer> closestKValues(TreeNode root, double target, int k) {
        LinkedList<Integer> re = new LinkedList<>();
        inorder(re, root, target, k);
        return re;
    }

    private void inorder(LinkedList<Integer> re, TreeNode root, double target, int k) {
        if (root != null) {
            inorder(re, root.left, target, k);
            if (re.size() == k) {
                if (Math.abs(root.val-target) < Math.abs(re.peekFirst()-target)) {
                    re.removeFirst();
                } else {
                    return;
                }
            }
            re.add(root.val);
            inorder(re, root.right, target, k);
        }

    }
}
