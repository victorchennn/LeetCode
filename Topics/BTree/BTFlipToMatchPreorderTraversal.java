package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BTFlipToMatchPreorderTraversal {
    private List<Integer> l = new ArrayList<>();
    private int index = 0;

    public List<Integer> flipMatchVoyage(TreeNode root, int[] voyage) {
        return dfs(root, voyage)? l: Arrays.asList(-1);
    }

    private boolean dfs(TreeNode root, int[] pre) {
        if (root == null) {
            return true;
        } else if (root.val != pre[index++]) {
            return false;
        } else if (root.left != null && root.left.val != pre[index]) {
            l.add(root.val);
            return dfs(root.right, pre) && dfs(root.left, pre);
        } else {
            return dfs(root.left, pre) && dfs(root.right, pre);
        }
    }
}
