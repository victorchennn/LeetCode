package Companies.Google;

import Libs.TreeNode;

public class BTLongestConsecutiveSequenceII {
    int max = 0;

    public int longestConsecutive(TreeNode root) {
        find(root);
        return max;
    }

    private int[] find(TreeNode root) {
        if (root == null) {
            return new int[]{0, 0};
        }
        int inc = 0, des = 0;
        if (root.left != null) {
            int[] l = find(root.left);
            if (root.val == root.left.val+1) {
                des = l[1]+1;
            }
            if (root.val == root.left.val-1) {
                inc = l[0]+1;
            }
        }
        if (root.right != null) {
            int[] r = find(root.right);
            if (root.val == root.right.val+1) {
                des = Math.max(des, r[1]+1);
            }
            if (root.val == root.right.val-1) {
                inc = Math.max(inc, r[0]+1);
            }
        }
        max = Math.max(max, des+inc+1);
        return new int[]{inc,des};
    }
}
