package Topics.BTree;

import Libs.TreeNode;

public class BTPathSum {
    public boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) {
            return false;
        }
//        LinkedList<TreeNode> node_stack = new LinkedList<>();
//        LinkedList<Integer> sum_stack = new LinkedList<>();
//        node_stack.add(root);
//        sum_stack.add(sum - root.val);
//
//        TreeNode node;
//        int curr_sum;
//        while (!node_stack.isEmpty()) {
//            node = node_stack.pollLast();
//            curr_sum = sum_stack.pollLast();
//            if ((node.right == null) && (node.left == null) && (curr_sum == 0))
//                return true;
//
//            if (node.right != null) {
//                node_stack.add(node.right);
//                sum_stack.add(curr_sum - node.right.val);
//            }
//            if (node.left != null) {
//                node_stack.add(node.left);
//                sum_stack.add(curr_sum - node.left.val);
//            }
//        }
//        return false;
        if (root.left == null && root.right == null && root.val == sum) {
            return true;
        }
        return hasPathSum(root.left, sum-root.val) || hasPathSum(root.right, sum-root.val);
    }
}
