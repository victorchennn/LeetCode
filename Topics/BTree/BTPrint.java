package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class BTPrint {
    public List<List<String>> printTree(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<List<String>> re = new ArrayList<>();
        int height = getHeight(root);
        int width = (int)Math.pow(2, height)-1;
        for (int i = 0; i < height; i++) {
            List<String> temp = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                temp.add("");
            }
            re.add(temp);
        }
        setTree(root, re, 0, width-1, height, 0);
        return re;
    }

    private int getHeight(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(getHeight(root.left), getHeight(root.right))+1;
    }

    private void setTree(TreeNode root, List<List<String>> re, int l, int r, int height, int level) {
        if (root == null || level == height) {
            return;
        }
        int mid = l + (r-l)/2;
        re.get(level).set(mid, String.valueOf(root.val));
        setTree(root.left, re, l, mid-1, height, level+1);
        setTree(root.right, re, mid+1, r, height, level+1);
    }
}
