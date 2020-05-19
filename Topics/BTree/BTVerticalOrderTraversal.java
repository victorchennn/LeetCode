package Topics.BTree;

import Libs.TreeNode;
import javafx.util.Pair;

import java.util.*;

/**
 * If two nodes are in the same row and column, the order should be from left to right.
 */
public class BTVerticalOrderTraversal {
    public List<List<Integer>> verticalOrder(TreeNode root) {
        List<List<Integer>> l = new ArrayList<>();
        if (root == null) {
            return l;
        }
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.add(new Pair<>(root, 0));
        Map<Integer, List<Integer>> m = new HashMap<>();
        int min = 0, max = 0;
        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            int col = pair.getValue();
            m.computeIfAbsent(col, k->new ArrayList<>()).add(node.val);
            if (node.left != null) {
                q.add(new Pair<>(node.left, col-1));
                min = Math.min(min, col-1);
            }
            if (node.right != null) {
                q.add(new Pair<>(node.right, col+1));
                max = Math.max(max, col+1);
            }
        }
        for (int i = min; i <= max; i++) {
            l.add(m.get(i));
        }
        return l;
    }
}
