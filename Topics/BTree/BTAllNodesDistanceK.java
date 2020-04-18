package Topics.BTree;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BTAllNodesDistanceK {
    Map<TreeNode, Integer> m = new HashMap<>();
//    Map<TreeNode, List<TreeNode>> m = new HashMap<>();

    public List<Integer> distanceK(TreeNode root, TreeNode target, int K) {
        List<Integer> re = new ArrayList<>();
        find(root, target);
        search(re, root, 0, K);
        return re;

//        if (root == null) {
//            return re;
//        }f
//        build(root, null);
//        Queue<TreeNode> q = new LinkedList<>();
//        q.add(target);
//        Set<TreeNode> set = new HashSet<>();
//        set.add(target);
//        while (!q.isEmpty()) {
//            int size = q.size();
//            if (K == 0) {
//                for (int i = 0; i < size; i++) {
//                    re.add(q.poll().val);
//                }
//                return re;
//            }
//            for (int i = 0; i < size; i++) {
//                TreeNode cur = q.poll();
//                for (TreeNode neigh : m.get(cur)) {
//                    if (set.contains(neigh)) {
//                        continue;
//                    }
//                    set.add(neigh);
//                    q.add(neigh);
//                }
//            }
//            K--;
//        }
//        return re;
    }

    private void find(TreeNode root, TreeNode target) {
        if (root == null) {
            return;
        }
        if (root == target) {
            m.put(root, 0);
            return;
        }
        find(root.left, target);
        if (m.containsKey(root.left)) {
            m.put(root, m.get(root.left)+1);
            return;
        }
        find(root.right, target);
        if (m.containsKey(root.right)) {
            m.put(root, m.get(root.right)+1);
            return;
        }
        return;
    }

    private void search(List<Integer> l, TreeNode root, int dis, int K) {
        if (root == null) {
            return;
        }
        if (m.containsKey(root)) {
            dis = m.get(root);
        }
        if (dis == K) {
            l.add(root.val);
        }
        search(l, root.left, dis+1, K);
        search(l, root.right, dis+1, K);
    }

//    private void build(TreeNode node, TreeNode parent) {
//        if (node != null) {
//            m.put(node, new ArrayList<>());
//            if (parent != null) {
//                m.get(node).add(parent);
//                m.get(parent).add(node);
//            }
//            build(node.left, node);
//            build(node.right, node);
//        }
//    }
}
