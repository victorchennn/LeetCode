package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BTFindDuplicateSubtrees {
    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        List<TreeNode> re = new ArrayList<>();
        dfs(re, new HashMap<>(), root);
        return re;
    }

    private String dfs(List<TreeNode> re, Map<String, Integer> m, TreeNode root) {
        if (root == null) {
            return "#";
        }
        String key = root.val + "," + dfs(re, m, root.left) + "," + dfs(re, m, root.right);
        if (m.getOrDefault(key, 0) == 1) {
            re.add(root);
        }
        m.put(key, m.getOrDefault(key, 0)+1);
        return key;
    }
}
