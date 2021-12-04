package Topics.BTree;

import Libs.Node;
import Libs.TreeNode;

public class BTLowestCommonAncestor {
    /**
     * guaranteed that both p and q are in the tree.
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) {
            return root;
        }
        return left == null? right:left;
    }

    int count = 0;

    /**
     * NOT guaranteed that both p and q are in the tree.
     */
    public TreeNode lowestCommonAncestorII(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode lca = helper(root, p, q);
        return count == 2? lca : null;
    }

    private TreeNode helper(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }
        TreeNode left = helper(root.left, p, q);
        TreeNode right = helper(root.right, p, q);
        if (root == p || root == q) {
            count++;
            return root;
        }
        return left == null? right : right == null? left : root;
    }

    /**
     * Each node will have a reference to its parent node.
     *
     * explanation:
     * 1 ------o---    1 + 2:  ------o-----o---
     * 2     --o---    2 + 1:  --o---------o---
     */
    public Node lowestCommonAncestorIII(Node p, Node q) {
        Node a = p, b = q;
        while (a != b) {
            a = a == null? q:a.parent;
            b = b == null? p:b.parent;
        }
        return a;
    }
}
