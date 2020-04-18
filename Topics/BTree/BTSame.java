package Topics.BTree;

import Libs.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

public class BTSame {
    public boolean isSameTree(TreeNode p, TreeNode q) {
        Queue<TreeNode> l = new LinkedList<>();
        l.add(p);
        l.add(q);
        while (!l.isEmpty()) {
            TreeNode t1 = l.poll();
            TreeNode t2 = l.poll();
            if (t1 == null && t2 == null) {
                continue;
            }
            if (t1 == null || t2 == null || t1.val != t2.val) {
                return false;
            }
            l.add(t1.left);
            l.add(t2.left);
            l.add(t1.right);
            l.add(t2.right);
        }
        return true;
    }

     public boolean isSameTreeII(TreeNode p, TreeNode q) {
         if (p == null && q == null) {
             return true;
         }
         if (p == null || q == null || p.val != q.val) {
             return false;
         }
         return isSameTreeII(p.left, q.left) && isSameTreeII(p.right, q.right);
     }
}
