package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class BTFindLeaves {
    public List<List<Integer>> findLeaves(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, root);
        return res;
    }

    private int dfs(List<List<Integer>> res, TreeNode root) {
        if(root == null) return -1;
        int left = dfs(res, root.left);
        int right = dfs(res, root.right);
        int height = 1 + Math.max(left, right);
        if(res.size() <= height) {
            res.add(new ArrayList<>());
        }
        res.get(height).add(root.val);
        return height;
    }
}
