package Topics.BTree;

import Libs.TreeNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BTCounsins {
    private Map<Integer, Integer> depths = new HashMap<>();
    private Map<Integer, TreeNode> parents = new HashMap<>();

    public boolean isCousins(TreeNode root, int x, int y) {
        if (root == null) {
            return false;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            int size = q.size();
            boolean findx = false, findy = false;
            for (int i = 0; i < size; i++) {
                TreeNode cur = q.poll();
                if (cur.val == x) findx = true;
                if (cur.val == y) findy = true;
                if (cur.left != null && cur.right != null) {
                    if ((cur.left.val == x && cur.right.val == y) || (cur.left.val == y && cur.right.val == x)) {
                        return false;
                    }
                }
                if (cur.left != null) q.add(cur.left);
                if (cur.right != null) q.add(cur.right);
            }
            if (findx && findy) {
                return true;
            } else if (findx || findy) {
                return false;
            }
        }
        return false;

//        dfs(root, null, 0);
//        return depths.get(x) == depths.get(y) && parents.get(x) != parents.get(y);
    }

    private void dfs(TreeNode root, TreeNode par, int depth) {
        if (root != null) {
            depths.put(root.val, depth);
            parents.put(root.val, par);
            dfs(root.left, root, depth+1);
            dfs(root.right, root, depth+1);
        }
    }
}
