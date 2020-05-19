package Topics.BTree;

import Libs.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

public class BTInvert {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            TreeNode temp = cur.left;
            cur.left = cur.right;
            cur.right = temp;
            if (cur.left != null) {
                q.add(cur.left);
            }
            if (cur.right != null) {
                q.add(cur.right);
            }
        }
        return root;


//        TreeNode right = invertTree(root.right);
//        TreeNode left = invertTree(root.left);
//        root.left = right;
//        root.right= left;
//        return root;
    }
}
