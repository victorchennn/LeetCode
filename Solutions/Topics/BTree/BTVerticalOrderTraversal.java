package Topics.BTree;

import Libs.TreeNode;

import java.util.*;

public class BTVerticalOrderTraversal {
    public List<List<Integer>> verticalOrder(TreeNode root) {
        List<List<Integer>> l = new ArrayList<>();
        if (root == null) {
            return l;
        }
        Queue<TreeNode> q1 = new LinkedList<>();
        Queue<Integer> q2 = new LinkedList<>();
        q1.add(root);
        q2.add(0);
        Map<Integer, List<Integer>> m = new HashMap<>();
        int min = 0, max = 0;
        while (!q1.isEmpty()) {
            TreeNode cur = q1.poll();
            int v = q2.poll();
            m.computeIfAbsent(v, k->new ArrayList<>()).add(cur.val);
            if (cur.left != null) {
                q1.add(cur.left);
                q2.add(v-1);
                min = Math.min(min, v-1);
            }
            if (cur.right != null) {
                q1.add(cur.right);
                q2.add(v+1);
                max = Math.max(max, v+1);
            }
        }
        for (int i = min; i <= max; i++) {
            l.add(m.get(i));
        }
        return l;
    }
}
