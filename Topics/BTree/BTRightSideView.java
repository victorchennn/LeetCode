package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class BTRightSideView {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> l = new ArrayList<>();
        helper(l, root, 0);
        return l;
    }

    private void helper(List<Integer> l, TreeNode root, int depth) {
        if (root != null) {
            if (depth == l.size()) {
                l.add(root.val);
            }
            helper(l, root.right, depth+1);
            helper(l, root.left, depth+1);
        }
    }

    public List<Integer> rightSideViewII(TreeNode root) {
        List<Integer> l = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        if (root == null) {
            return l;
        }
        q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                TreeNode cur = q.poll();
                if (i == 0) {
                    l.add(cur.val);
                }
                if (cur.right != null) {
                    q.add(cur.right);
                }
                if (cur.left != null) {
                    q.add(cur.left);
                }
            }
        }
        return l;
    }
}
