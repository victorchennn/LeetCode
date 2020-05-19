package Topics.BTree;

import Libs.TreeNode;

import java.util.*;

public class BTLevelOrderTraversal {
    public List<List<Integer>> levelOrder(TreeNode root) {
//        List<List<Integer>> re = new ArrayList<>();
//        if (root == null) {
//            return re;
//        }
//        Queue<TreeNode> q = new LinkedList<>();
//        q.add(root);
//        while (!q.isEmpty()) {
//            int size = q.size();
//            re.add(new ArrayList<>());
//            for (int i = 0; i < size; i++) {
//                TreeNode cur = q.poll();
//                re.get(re.size()-1).add(cur.val);
//                if (cur.left != null) {
//                    q.add(cur.left);
//                }
//                if (cur.right != null) {
//                    q.add(cur.right);
//                }
//            }
//        }
//        return re;

        List<List<Integer>> re = new ArrayList<>();
        helper(re, root, 0);
        return re;
    }

    private void helper(List<List<Integer>> re, TreeNode root, int level) {
        if (root != null) {
            if (level == re.size()) {
                re.add(new ArrayList<>());
            }
            re.get(level).add(root.val);
            helper(re, root.left, level+1);
            helper(re, root.right, level+1);
        }
    }
}
