package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class BTPathSumII {
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> re = new ArrayList<>();
        if (root == null) {
            return re;
        }
        List<Integer> l = new ArrayList<>();
        l.add(root.val);
        dfs(re, l, root, sum-root.val);
        return re;
    }

    private void dfs(List<List<Integer>> re, List<Integer> l, TreeNode root, int sum) {
        if (root.left == null && root.right == null) {
            if (sum == 0) {
                re.add(new ArrayList<>(l));
            }
            return;
        }
        if (root.left != null) {
            l.add(root.left.val);
            dfs(re, l, root.left, sum-root.left.val);
            l.remove(l.size()-1);
        }
        if (root.right != null) {
            l.add(root.right.val);
            dfs(re, l, root.right, sum-root.right.val);
            l.remove(l.size()-1);
        }
    }
}
