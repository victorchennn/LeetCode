package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class BSTsTwoSum {
    public boolean twoSumBSTs(TreeNode root1, TreeNode root2, int target) {
        if (root1 == null) {
            return false;
        }
        if (find(root2, target-root1.val)) {
            return true;
        }
        return twoSumBSTs(root1.left, root2, target) || twoSumBSTs(root1.right, root2, target);

//        Deque<TreeNode> stack = new ArrayDeque<>();
//        Set<Integer> s = new HashSet<>();
//        while (!stack.isEmpty() || root1 != null) {
//            while (root1 != null) {
//                stack.push(root1);
//                root1 = root1.left;
//            }
//            root1 = stack.pop();
//            s.add(target-root1.val);
//            root1 = root1.right;
//        }
//
//        while (!stack.isEmpty() || root2 != null) {
//            while (root2 != null) {
//                stack.push(root2);
//                root2 = root2.left;
//            }
//            root2 = stack.pop();
//            if (s.contains(root2.val)) {
//                return true;
//            }
//            root2 = root2.right;
//        }
//        return false;
    }

    private boolean find(TreeNode root, int target) {
        while (root != null) {
            if (root.val == target) {
                return true;
            } else if (root.val > target) {
                root = root.left;
            } else {
                root = root.right;
            }
        }
        return false;
    }
}
