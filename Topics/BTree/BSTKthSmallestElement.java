package Topics.BTree;

import Libs.TreeNode;

import java.util.Stack;

public class BSTKthSmallestElement {
    public int kthSmallest(TreeNode root, int k) {
        int index = 0;
        Stack<TreeNode> s = new Stack<>();
        while (!s.isEmpty() || root != null) {
            while (root != null) {
                s.push(root);
                root = root.left;
            }
            root = s.pop();
            index++;
            if (index == k) {
                return root.val;
            }
            root = root.right;
        }
        return -1;
    }
}
