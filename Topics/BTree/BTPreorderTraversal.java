package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BTPreorderTraversal {

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> re = new ArrayList<>();
        if (root == null) {
            return re;
        }
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            root = stack.pop();
            re.add(root.val);
            if (root.right != null) {
                stack.push(root.right);
            }
            if (root.left != null) {
                stack.push(root.left);
            }
        }
        return re;
    }

    private void helper(List<Integer> re, TreeNode root) {
        if (root != null) {
            re.add(root.val);
            helper(re, root.left);
            helper(re, root.right);
        }
    }
}
