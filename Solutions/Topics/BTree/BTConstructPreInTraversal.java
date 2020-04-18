package Topics.BTree;

import Libs.TreeNode;

import java.util.HashMap;
import java.util.Map;

public class BTConstructPreInTraversal {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            m.put(inorder[i], i);
        }
        return helper(m, 0, 0, inorder.length-1, preorder, inorder);
    }

    private TreeNode helper(Map<Integer, Integer> m, int pres, int ins, int ine, int[] preorder, int[] inorder) {
        if (ins > ine) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[pres]);
        int rootIndex = m.get(root.val);
        root.left = helper(m, pres+1, ins, rootIndex-1, preorder, inorder);
        root.right = helper(m, pres+1+rootIndex-ins, rootIndex+1, ine, preorder, inorder);
        return root;
    }
}
