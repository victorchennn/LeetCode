package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class BSTBalance {
    public TreeNode balanceBST(TreeNode root) {
        List<TreeNode> list = new ArrayList<>();
        inorder(list, root);
        return balanced(list, 0, list.size());
    }

    private void inorder(List<TreeNode> list, TreeNode root) {
        if (root == null) {
            return;
        }
        inorder(list, root.left);
        list.add(root);
        inorder(list, root.right);
    }

    private TreeNode balanced(List<TreeNode> list, int l, int r) {
        if (l >= r) {
            return null;
        }
        int mid = l + (r-l)/2;
        list.get(mid).left = balanced(list, l, mid);
        list.get(mid).right = balanced(list, mid+1, r);
        return list.get(mid);
    }
}
