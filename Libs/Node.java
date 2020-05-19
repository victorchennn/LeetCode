package Libs;

import java.util.List;

public class Node {
    public int val;
    public Node left;
    public Node right;
    public Node parent;
    public Node child;
    public Node prev;
    public Node next;
    public Node random;
    public List<Node> children;
    public List<Node> neighbors;

    public Node() {}

    public Node(int _val, Node _next) {
        val = _val;
        next = _next;
    }

    public Node(int _val) {
        this.val = _val;
        this.next = null;
        this.random = null;
    }

    public Node(int _val, List<Node> _children) {
        val = _val;
        children = _children;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
}
