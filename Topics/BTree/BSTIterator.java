package Topics.BTree;

import Libs.TreeNode;

import java.util.Stack;

public class BSTIterator {
    private Stack<TreeNode> s;

    public BSTIterator(TreeNode root) {
        s = new Stack<>();
        add(root);
    }

    private void add(TreeNode root) {
        while (root != null) {
            s.add(root);
            root = root.left;
        }
    }

    /** @return the next smallest number */
    public int next() {
        TreeNode t = s.pop();
        add(t.right);
        return t.val;
    }

    /** @return whether we have next smallest number */
    public boolean hasNext() {
        return !s.isEmpty();
    }
}
