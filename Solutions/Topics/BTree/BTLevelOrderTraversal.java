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

        Map<Integer, List<Integer>> m = new HashMap<>();
        helper(m, root, 0);
        return new ArrayList<>(m.values());
    }

    private void helper(Map<Integer, List<Integer>> m, TreeNode root, int level) {
        if (root != null) {
            m.computeIfAbsent(level, k-> new ArrayList<>()).add(root.val);
            helper(m, root.left, level+1);
            helper(m, root.right, level+1);
        }
    }
}
