package Companies.ServiceNow;

import Libs.TreeNode;

import java.util.*;

public class TwoSumInBST {
    public boolean findTarget_s1(TreeNode root, int k) {
        return helper(new HashSet<>(), root, k);
    }

    public boolean findTarget_s2(TreeNode root, int k) {
        if (root == null) {
            return false;
        }
        Set<Integer> s = new HashSet<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            if (s.contains(k-cur.val)) {
                return true;
            }
            s.add(cur.val);
            if (cur.left != null) {
                q.add(cur.left);
            }
            if (cur.right != null) {
                q.add(cur.right);
            }
        }
        return false;
    }

    public boolean findTarget_s3(TreeNode root, int k) {
        List<Integer> l = new ArrayList<>();
        inOrder(l, root);
        int left = 0, right = l.size()-1;
        while (left < right) {
            if (l.get(left) + l.get(right) == k) {
                return true;
            } else if (l.get(left) + l.get(right) < k) {
                left++;
            } else {
                right--;
            }
        }
        return false;
    }

    private void inOrder(List<Integer> l, TreeNode root) {
        if (root == null) {
            return;
        }
        inOrder(l, root.left);
        l.add(root.val);
        inOrder(l, root.right);
    }


    private boolean helper(Set<Integer> s, TreeNode root, int k) {
        if (root == null) {
            return false;
        }
        if (s.contains(k-root.val)) {
            return true;
        }
        s.add(root.val);
        return helper(s, root.left, k) || helper(s, root.right, k);
    }
}
