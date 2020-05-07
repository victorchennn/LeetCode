package Companies.Amazon;

import Libs.Node;

import java.util.*;

public class CloneGraph {
    public Node cloneGraph(Node node) {
        if (node == null) {
            return null;
        }
        Map<Node, Node> visited = new HashMap<>();
        Queue<Node> q = new LinkedList<>();
        q.add(node);
        visited.put(node, new Node(node.val, new ArrayList<>()));
        while (!q.isEmpty()){
            Node cur = q.poll();
            for (Node neigh : cur.neighbors) {
                if (!visited.containsKey(neigh)) {
                    visited.put(neigh, new Node(neigh.val, new ArrayList<>()));
                    q.add(neigh);
                }
                visited.get(cur).neighbors.add(visited.get(neigh));
            }
        }
        return visited.get(node);

//        m = new HashMap<>();
//        return helper(m, node);
    }

    private Node helper(Map<Integer, Node> m, Node node) {
        if (node == null) {
            return null;
        }
        if (m.containsKey(node.val)) {
            return m.get(node.val);
        }
        Node newone = new Node(node.val, new ArrayList<>());
        m.put(newone.val, newone);
        for (Node n : node.neighbors) {
            newone.neighbors.add(helper(m, n));
        }
        return newone;
    }
}
