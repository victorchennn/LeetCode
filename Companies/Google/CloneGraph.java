package Companies.Google;

import Libs.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CloneGraph {

    Map<Integer, Node> m;

    public Node cloneGraph(Node node) {
        m = new HashMap<>();
        return helper(node);
    }

    private Node helper(Node node) {
        if (node == null) {
            return null;
        }
        if (m.containsKey(node.val)) {
            return m.get(node.val);
        }
        Node newone = new Node(node.val, new ArrayList<>());
        m.put(newone.val, newone);
        for (Node n : node.neighbors) {
            newone.neighbors.add(helper(n));
        }
        return newone;
    }
}
