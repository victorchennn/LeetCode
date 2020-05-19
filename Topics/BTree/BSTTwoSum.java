package Topics.BTree;

import Libs.TreeNode;

import java.util.*;

public class BSTTwoSum {

    public boolean findTarget(TreeNode root, int k) {
        Set<Integer> s = new HashSet<>();
        return find(root, s, k);

//        Set<Integer> s = new HashSet<>();
//        Queue<TreeNode> q = new LinkedList<>();
//        q.add(root);
//        while (!q.isEmpty()) {
//            TreeNode cur = q.poll();
//            if (s.contains(k-cur.val)) {
//                return true;
//            }
//            s.add(cur.val);
//            if (cur.left != null) {
//                q.add(cur.left);
//            }
//            if (cur.right != null) {
//                q.add(cur.right);
//            }
//        }
//        return false;
    }

    private boolean find(TreeNode root, Set<Integer> set, int k) {
        if (root == null) {
            return false;
        }
        if (set.contains(k-root.val)) {
            return true;
        }
        set.add(root.val);
        return find(root.left, set, k) || find(root.right, set, k);
    }

    public boolean findTargetII(TreeNode root, int k) {
        List<Integer> list = new ArrayList<>();
        inorder(list, root);
        int l = 0, r = list.size()-1;
        while (l < r) {
            int sum = list.get(l) + list.get(r);
            if (sum == k) {
                return true;
            }
            if (sum > k) {
                r--;
            } else {
                l++;
            }
        }
        return false;
    }

    private void inorder(List<Integer> l, TreeNode root) {
        if (root != null) {
            inorder(l, root.left);
            l.add(root.val);
            inorder(l, root.right);
        }
    }
}
