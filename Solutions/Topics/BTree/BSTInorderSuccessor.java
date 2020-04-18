package Topics.BTree;

import Libs.Node;
import Libs.TreeNode;

public class BSTInorderSuccessor {
    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        if (root == null) {
            return null;
        }
        if (p.val >= root.val) {
            return inorderSuccessor(root.right, p);
        }
        TreeNode left = inorderSuccessor(root.left, p);
        return left == null? root:left;
    }

    public TreeNode inorderSuccessorII(TreeNode root, TreeNode p) {
        TreeNode parent = null;
        while (root != null) {
            if (p.val < root.val) {
                parent = root;
                root = root.left;
            } else {
                root = root.right;
            }
        }
        return parent;
    }

    public Node inorderSuccessor(Node node) {
        if (node.right != null) {
            Node re = node.right;
            while (re.left != null) {
                re = re.left;
            }
            return re;
        } else {
            Node re = node;
            while (re.parent != null && re == re.parent.right) {
                re = re.parent;
            }
            return re.parent;
        }
    }
}
