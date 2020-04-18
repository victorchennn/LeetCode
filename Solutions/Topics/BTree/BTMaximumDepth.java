package Topics.BTree;

import Libs.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

public class BTMaximumDepth {
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        int depth = 0;
        while (!q.isEmpty()) {
            depth++;
            int size = q.size();
            while (size-- > 0) {
                TreeNode cur = q.poll();
                if (cur.left != null) {
                    q.add(cur.left);
                }
                if (cur.right != null) {
                    q.add(cur.right);
                }
            }
        }
        return depth;

        // return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }
}
