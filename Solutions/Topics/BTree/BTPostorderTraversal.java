package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class BTPostorderTraversal {
    public List<Integer> postorderTraversal(TreeNode root) {
        LinkedList<Integer> re = new LinkedList<>();
        if (root == null) {
            return re;
        }
        Deque<TreeNode> stack = new ArrayDeque<>();
        while (!stack.isEmpty() || root != null) {
            if (root != null) {
                stack.push(root);
                re.addFirst(root.val);
                root = root.right;
            } else {
                root = stack.pop();
                root = root.left;
            }
        }
        return re;
    }

    private void post(List<Integer> re, TreeNode root) {
        if (root != null) {
            post(re, root.left);
            post(re, root.right);
            re.add(root.val);
        }
    }
}
