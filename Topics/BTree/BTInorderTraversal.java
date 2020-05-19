package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BTInorderTraversal {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> re = new ArrayList<>();
        Stack<TreeNode> s = new Stack<>();
        while (root != null || !s.isEmpty()) {
            while (root != null) {
                s.push(root);
                root = root.left;
            }
            root = s.pop();
            re.add(root.val);
            root = root.right;
        }
        return re;
    }

    private void inorder(List<Integer> l, TreeNode root) {
        if (root != null) {
            inorder(l, root.left);
            l.add(root.val);
            inorder(l, root.right);
        }
    }
}
