package Companies.Google;

import Libs.TreeNode;

import java.util.Arrays;

public class ConstructBTfromPreAndPostTraversal {
    public TreeNode constructFromPrePost(int[] pre, int[] post) {
        if (pre.length == 0) {
            return null;
        }
        TreeNode root = new TreeNode(pre[0]);
        if (pre.length == 1) {
            return root;
        }
        int i = 0;
        while (post[i] != pre[1]) {
            i++;
        }
        root.left = constructFromPrePost(Arrays.copyOfRange(pre, 1, i+2),
                Arrays.copyOfRange(post, 0, i+1));
        root.right = constructFromPrePost(Arrays.copyOfRange(pre, i+2, pre.length),
                Arrays.copyOfRange(post, i+1, post.length-1));
        return root;
    }
}
