package Topics.BTree;

import Libs.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

public class BTSumofLeftLeaves {
    public int sumOfLeftLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int sum = 0;
        if (root.left != null) {
            if (root.left.left == null && root.left.right == null) {
                sum += root.left.val;
            } else {
                sum += sumOfLeftLeaves(root.left);
            }
        }
        sum += sumOfLeftLeaves(root.right);
        return sum;


//        Queue<TreeNode> q = new LinkedList<>();
//        q.add(root);
//        while (!q.isEmpty()) {
//            TreeNode cur = q.poll();
//            if (cur.left != null) {
//                if (cur.left.left == null && cur.left.right == null) {
//                    sum += cur.left.val;
//                } else {
//                    q.add(cur.left);
//                }
//            }
//            if (cur.right != null) {
//                if (cur.right.left != null || cur.right.right != null) {
//                    q.add(cur.right);
//                }
//            }
//        }
//        return sum;
    }
}
