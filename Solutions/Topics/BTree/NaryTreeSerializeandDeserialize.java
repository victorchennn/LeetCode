package Topics.BTree;

import Libs.Node;

import java.util.*;

public class NaryTreeSerializeandDeserialize {

    // Encodes a tree to a single string.
    public String serialize(Node root) {
        List<String> l = new ArrayList<>();
        helperS(l, root);
        return String.join(",", l);
    }

    private void helperS(List<String> l, Node root) {
        if (root == null) {
            return;
        }
        l.add(String.valueOf(root.val));
        l.add(String.valueOf(root.children.size()));
        for (Node child : root.children) {
            helperS(l, child);
        }
    }

    // Decodes your encoded data to tree.
    public Node deserialize(String data) {
        if (data.length() == 0) {
            return null;
        }
        Queue<String> q = new LinkedList<>(Arrays.asList(data.split(",")));
        return helperD(q);
    }

    private Node helperD(Queue<String> q) {
        Node root = new Node(Integer.valueOf(q.poll()));
        int size = Integer.valueOf(q.poll());
        root.children = new ArrayList<>(size);
        for(int i = 0 ;i < size; i++){
            root.children.add(helperD(q));
        }
        return root;

    }
}
