package Companies.Google;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeleteNodesAndReturnForest {

    List<TreeNode> re;
    Set<Integer> delete;

    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        re = new ArrayList<>();
        delete = new HashSet<>();
        for (int i : to_delete) {
            delete.add(i);
        }
        helper(root, true);
        return re;
    }

    private TreeNode helper(TreeNode root , boolean isroot) {
        if (root == null) {
            return null;
        }
        boolean to_delete = delete.contains(root.val);
        if (isroot && !to_delete) {
            re.add(root);
        }
        root.left = helper(root.left, to_delete);
        root.right = helper(root.right, to_delete);
        return to_delete?null:root;
    }
}
