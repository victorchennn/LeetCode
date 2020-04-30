package Topics.BTree;

import Libs.TreeNode;
import javafx.util.Pair;

import java.util.ArrayDeque;
import java.util.Deque;

public class BTRootToLeafNumbersSum {
    public int sumNumbers(TreeNode root) {
//        int sum = 0;
//        Deque<Pair<TreeNode, Integer>> q = new ArrayDeque<>();
//        q.add(new Pair<>(root, 0));
//        while (!q.isEmpty()) {
//            Pair<TreeNode, Integer> p = q.pop();
//            TreeNode cur = p.getKey();
//            int value = p.getValue();
//            if (cur != null) {
//                value = value*10 + root.val;
//                if (cur.left == null && cur.right == null) {
//                    sum += value;
//                } else {
//                    q.push(new Pair<>(root.left, value));
//                    q.push(new Pair<>(root.right, value));
//                }
//            }
//        }

        return sum(root, 0);
    }

    private int sum(TreeNode root, int sum) {
        if (root == null) {
            return 0; // not return sum
        }
        if (root.left == null && root.right == null) {
            return sum*10 + root.val;
        }
        return sum(root.left, sum*10 + root.val) + sum(root.right, sum*10 + root.val);
    }
}
