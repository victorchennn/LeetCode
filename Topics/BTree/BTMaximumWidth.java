package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BTMaximumWidth {
    private int max = 0;

    public int widthOfBinaryTree(TreeNode root) {
        List<Integer> l = new ArrayList<>();
        helper(l, 0, 1, root);
        return max;

//        if (root == null) {
//            return 0;
//        }
//        List<Integer> heads = new ArrayList<>();
//        Queue<TreeNode> q = new LinkedList<>();
//        Queue<Integer> in = new LinkedList<>();
//        q.add(root);
//        in.add(1);
//        while (!q.isEmpty()) {
//            int size = q.size();
//            for (int i = 0; i < size; i++) {
//                TreeNode cur = q.poll();
//                int index = in.poll();
//                if (i == 0) {
//                    heads.add(index);
//                }
//                if (i == size-1) {
//                    max = Math.max(max, index-heads.get(heads.size()-1)+1);
//                }
//                if (cur.left != null) {
//                    q.add(cur.left);
//                    in.add(index*2);
//                }
//                if (cur.right != null) {
//                    q.add(cur.right);
//                    in.add(index*2+1);
//                }
//            }
//        }
//        return max;
    }

    private void helper(List<Integer> l, int level, int index, TreeNode root) {
        if (root != null) {
            if (level == l.size()) {
                l.add(index);
            }
            max = Math.max(max, index-l.get(level)+1);
            helper(l, level+1, index*2, root.left);
            helper(l, level+1, index*2+1, root.right);
        }
    }
}
