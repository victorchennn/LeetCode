package Topics.BTree;

import Libs.TreeNode;

import java.util.HashMap;
import java.util.Map;

public class BTConstructInPostTraversal {
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            m.put(inorder[i], i);
        }
        return helper(m, postorder.length-1, 0, inorder.length-1, inorder, postorder);
    }

    private TreeNode helper(Map<Integer, Integer> m, int poste, int ins, int ine, int[] inorder, int[] postorder) {
        if (ins > ine) {
            return null;
        }
        TreeNode root = new TreeNode(postorder[poste]);
        int rootIndex = m.get(root.val);
        root.left = helper(m, poste-1-(ine-rootIndex), ins, rootIndex-1, inorder, postorder);
        root.right = helper(m, poste-1, rootIndex+1, ine, inorder, postorder);
        return root;
    }
}
