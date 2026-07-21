// Given the root of a binary tree, return the vertical order traversal of its nodes' values. (i.e., from top to bottom, column by column).

// If two nodes are in the same row and column, the order should be from left to right.

  
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }

private TreeMap<Integer, List<int[]>> m = new TreeMap<>();

public List<List<Integer>> verticalOrder(TreeNode root) {
    dfs(root, 0, 0);

    List<List<Integer>> result = new ArrayList<>();

    for (List<int[]> cols : m.values()) {
        Collections.sort(cols, (a,b)->a[0]-b[0]);

        List<Integer> columnValues = new ArrayList<>();
        for (int[] node: cols) {
            columnValues.add(node[1]);
        }

        result.add(columnValues);
    }

    return result;
}

private void dfs(TreeNode root, int col, int depth) {
    if (root == null) {
        return;
    }
    m.computeIfAbsent(col, k->new ArrayList<>()).add(new int[]{depth, root.val});

    dfs(root.left, col-1, depth+1);
    dfs(root.right, col+1,  depth+1);
}
