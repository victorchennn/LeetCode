package Companies.Bloomberg;

import java.util.Arrays;
import java.util.List;

public class CountofSmallerNumbersAfterSelf {
    public List<Integer> countSmaller(int[] nums) {
        Integer[] count = new Integer[nums.length];
        if (nums.length < 1) {
            return Arrays.asList(count);
        }
        Node root = new Node(nums[nums.length-1]);
        for (int i = nums.length-1; i >= 0; i--) {
            count[i] = insert(root, nums[i]);
        }
        return Arrays.asList(count);
    }

    private int insert(Node root, int val) {
        int sum = 0;
        while (val != root.val) {
            if (val > root.val) {
                sum += root.leftcount + root.count;
                if (root.right == null) {
                    root.right = new Node(val);
                }
                root = root.right;
            } else {
                root.leftcount++;
                if (root.left == null) {
                    root.left = new Node(val);
                }
                root = root.left;
            }
        }
        root.count++;
        return root.leftcount + sum;
    }

    class Node {
        int val, count = 0, leftcount = 0;
        Node left, right;
        public Node(int val) {
            this.val = val;
        }
    }
}
