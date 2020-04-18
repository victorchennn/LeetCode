package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class BSTUnique {

    /* Given n = 3, there are a total of 5 unique BSTs */
    public int numTrees(int n) {
        int[] nums = new int[n+1];
        nums[0] = nums[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int left = 0; left < i; left++) {
                nums[i] += nums[left]*nums[i-1-left];
            }
        }
        return nums[n];
    }

    /* generate all structurally unique BSTs */
    public List<TreeNode> generateTrees(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }
        return helper(1, n);
    }

    private List<TreeNode> helper(int l, int r) {
        List<TreeNode> re = new ArrayList<>();
        if (l > r) {
            re.add(null);
        }
        for (int m = l; m <= r; m++) {
            List<TreeNode> lefts = helper(l, m-1);
            List<TreeNode> rights = helper(m+1, r);
            for (TreeNode left : lefts) {
                for (TreeNode right : rights) {
                    TreeNode root = new TreeNode(m);
                    root.left = left;
                    root.right = right;
                    re.add(root);
                }
            }
        }
        return re;
    }
}
