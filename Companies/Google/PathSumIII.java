package Companies.Google;

import Libs.TreeNode;

import java.util.HashMap;
import java.util.Map;

public class PathSumIII {
    public int pathSum(TreeNode root, int sum) {
        Map<Integer, Integer> m = new HashMap<>();
        m.put(0, 1);
        return helper(m, root, sum, 0);
    }

    private int helper(Map<Integer, Integer> m, TreeNode root, int sum, int cur) {
        if (root == null) {
            return 0;
        }
        cur += root.val;
        int re = m.getOrDefault(cur-sum, 0);
        m.put(cur, m.getOrDefault(cur, 0)+1);
        re += helper(m, root.left, sum, cur) + helper(m, root.right, sum, cur);
        m.put(cur, m.get(cur)-1);
        return re;
    }
}
