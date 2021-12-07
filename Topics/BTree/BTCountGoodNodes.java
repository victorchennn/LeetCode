package Topics.BTree;

import Libs.TreeNode;

import java.util.Stack;

/**
 * Given a binary tree root, a node X in the tree is named good if in the path from root to X
 * there are no nodes with a value greater than X.
 *
 * Return the number of good nodes in the binary tree.
 */
public class BTCountGoodNodes {
    /* Recursion */
    public int goodNodes(TreeNode root) {
        return dfs(root, root.val);
    }

    private int dfs(TreeNode root, int max) {
        if (root == null) {
            return 0;
        }
        int count = 0;
        if (root.val >= max) {
            count += 1;
        }
        count += dfs(root.left, Math.max(max, root.val));
        count += dfs(root.right, Math.max(max, root.val));
        return count;
    }

    /* Iteration */
    public int goodNodesII(TreeNode root) {
        int numGoodNodes = 0;
        Stack<Pair> stack = new Stack<>();
        stack.push(new Pair(root, Integer.MIN_VALUE));

        while (stack.size() > 0) {
            Pair curr = stack.pop();
            if (curr.maxSoFar <= curr.node.val) {
                numGoodNodes++;
            }

            if (curr.node.left != null) {
                stack.push(new Pair(curr.node.left, Math.max(curr.node.val, curr.maxSoFar)));
            }

            if (curr.node.right != null) {
                stack.push(new Pair(curr.node.right, Math.max(curr.node.val, curr.maxSoFar)));
            }
        }

        return numGoodNodes;
    }

    class Pair {
        TreeNode node;
        int maxSoFar;

        Pair(TreeNode node, int maxSoFar) {
            this.node = node;
            this.maxSoFar = maxSoFar;
        }
    }
}
