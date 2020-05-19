package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class BTBoundary {
    public List<Integer> boundaryOfBinaryTree(TreeNode root) {
        List<Integer> l = new ArrayList<>();
        if (root == null) {
            return l;
        }
        l.add(root.val);
        addLeft(root.left, l);
        addLeaves(root.left, l);
        addLeaves(root.right, l);
        addRight(root.right, l);
        return l;
    }

    private void addLeft(TreeNode root, List<Integer> l) {
        if (root == null || (root.left == null && root.right == null)) {
            return;
        }
        l.add(root.val);
        if (root.left != null) {
            addLeft(root.left, l);
        } else {
            addLeft(root.right, l);
        }
    }

    private void addRight(TreeNode root, List<Integer> l) {
        if (root == null || (root.left == null && root.right == null)) {
            return;
        }
        if (root.right != null) {
            addRight(root.right, l);
        } else {
            addRight(root.left, l);
        }
        l.add(root.val);
    }

    private void addLeaves(TreeNode root, List<Integer> l) {
        if (root == null) {
            return;
        }
        if (root.left == null && root.right == null) {
            l.add(root.val);
            return;
        }
        addLeaves(root.left, l);
        addLeaves(root.right, l);
    }
}
