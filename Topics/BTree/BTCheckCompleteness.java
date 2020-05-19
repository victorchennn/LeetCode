package Topics.BTree;

import java.util.LinkedList;
import java.util.Queue;
import Libs.TreeNode;

public class BTCheckCompleteness {
    public boolean isCompleteTree(TreeNode root) {
        if (root == null) {
            return true;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (q.peek() != null) { // !!!
            TreeNode cur = q.poll();
            if (cur.right != null && cur.left == null) {
                return false;
            }
            q.add(cur.left);
            q.add(cur.right);
        }
        while (!q.isEmpty() && q.peek() == null) {
            q.poll();
        }
        return q.isEmpty();
    }
}
