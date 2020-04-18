package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BTZigzagLevelOrderTraversal {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        Map<Integer, List<Integer>> m = new HashMap<>();
        helper(m, root, 0);
        return new ArrayList<>(m.values());
    }

    private void helper(Map<Integer, List<Integer>> m, TreeNode root, int level) {
        if (root != null) {
            m.computeIfAbsent(level, k-> new ArrayList<>());
            if (level % 2 == 0) {
                m.get(level).add(root.val);
            } else {
                m.get(level).add(0, root.val);
            }
            helper(m, root.left, level+1);
            helper(m, root.right, level+1);
        }
    }
}
