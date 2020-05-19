package Topics.Design;

import Libs.TreeNode;

/**
 * Time Complexity: O(log(N/K)), N is the number of all possible values, K is the number of predefined size
 * Space Complexity: O(K+M), M is the number of unique values inserted
 */
public class MyHashSet {
    private Bucket[] bucketArray;
    private int keyRange;

    public MyHashSet() {
        keyRange = 769; // prime number as the base of modulo to reduce potential collisions
        bucketArray = new Bucket[keyRange];
        for (int i = 0; i < keyRange; i++) {
            bucketArray[i] = new Bucket();
        }
    }

    public void add(int key) {
        bucketArray[_hash(key)].insert(key);
    }

    public void remove(int key) {
        bucketArray[_hash(key)].delete(key);
    }

    public boolean contains(int key) {
        return bucketArray[_hash(key)].exists(key);
    }

    private int _hash(int key) {
        return key%keyRange;
    }

    class Bucket {
        private BSTree tree;

        public Bucket() {
            tree = new BSTree();
        }

        void insert(Integer key) {
            tree.root = tree.insert(tree.root, key);
        }

        void delete(Integer key) {
            tree.root = tree.delete(tree.root, key);
        }

        boolean exists(Integer key) {
            return tree.searchBST(tree.root, key) != null;
        }
    }


    class BSTree {
        TreeNode root = null;

        TreeNode searchBST(TreeNode root, int val) {
            if (root == null || val == root.val) {
                return root;
            }
            return val < root.val? searchBST(root.left, val):searchBST(root.right, val);
        }

        TreeNode insert(TreeNode root, int val) {
            if (root == null) {
                return new TreeNode(val);
            }
            if (val > root.val) {
                root.right = insert(root.right, val);
            } else if (val < root.val) {
                root.left = insert(root.left, val);
            } else {
                return root;
            }
            return root;
        }

        int predecessor(TreeNode root) {
            root = root.left;
            while (root.right != null) {
                root = root.right;
            }
            return root.val;
        }

        int successor(TreeNode root) {
            root = root.right;
            while (root.left != null) {
                root = root.left;
            }
            return root.val;
        }

        TreeNode delete(TreeNode root, int key) {
            if (root == null) {
                return null;
            }
            if (key > root.val) {
                root.right = delete(root.right, key);
            } else if (key < root.val) {
                root.left = delete(root.left, key);
            } else {
                if (root.left == null && root.right == null) {
                    root = null;
                } else if (root.right != null) {
                    root.val = successor(root);
                    root.right = delete(root.right, root.val);
                } else {
                    root.val = predecessor(root);
                    root.left = delete(root.left, root.val);
                }
            }
            return root;
        }
    }
}
