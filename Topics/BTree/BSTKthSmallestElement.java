package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Follow up: What if the BST is modified (insert/delete operations) often and
 *            you need to find the kth smallest frequently?
 *
 * Let's use here the same logic as for LRU cache design, and combine an indexing structure
 * (we could keep BST here) with a double linkedlist.
 * O(logN) time for the insert and delete, O(k) for search
 * O(logN + k) for insert/delete + search of kth smallest, instead of O(2logN + k)
 */
public class BSTKthSmallestElement {
    /**
     * Time complexity: O(logN + k), since before starting to pop out one has to go down to a leaf.
     * Space complexity: O(logN + k)
     */
    public int kthSmallest(TreeNode root, int k) {
        int index = 0;
        Stack<TreeNode> s = new Stack<>();
        while (!s.isEmpty() || root != null) {
            while (root != null) {
                s.push(root);
                root = root.left;
            }
            root = s.pop();
            index++;
            if (index == k) {
                return root.val;
            }
            root = root.right;
        }
        return -1;

//        List<Integer> re = new ArrayList<>();
//        inorder(root, re, k);
//        return re.get(k - 1);
    }

    private void inorder(TreeNode root, List<Integer> arr, int k) {
        if (root != null && arr.size() < k) {
            inorder(root.left, arr, k);
            arr.add(root.val);
            inorder(root.right, arr, k);
        }
    }
}
