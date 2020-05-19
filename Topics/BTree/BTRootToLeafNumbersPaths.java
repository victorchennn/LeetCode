package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class BTRootToLeafNumbersPaths {
    public static List<Integer> paths(TreeNode root) {
        List<Integer> re = new ArrayList<>();
        if (root == null) {
            return re;
        }
        dfs(root, re, 0);
        return re;
    }

    private static void dfs(TreeNode root, List<Integer> l, int path) {
        if (root.left == null && root.right == null) {
            l.add(path*10 + root.val);
            return;
        }
        if (root.left != null) {
            dfs(root.left, l, path*10 + root.val);
        }
        if (root.right != null) {
            dfs(root.right, l, path*10 + root.val);
        }
    }
}
