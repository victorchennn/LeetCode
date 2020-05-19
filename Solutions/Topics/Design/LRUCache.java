package Topics.Design;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    private Node head, tail;
    private final int cap;
    private Map<Integer, Node> m;

    public LRUCache(int capacity) {
        cap = capacity;
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
        m = new HashMap<>();
    }

    public int get(int key) {
        if (!m.containsKey(key)) {
            return -1;
        }
        Node node = m.get(key);
        update(node);
        return node.val;
    }

    public void put(int key, int value) {
        if (m.containsKey(key)) {
            Node node = m.get(key);
            node.val = value;
            update(node);
        } else {
            if (m.size() == cap) {
                delete(tail.prev);
            }
            add(new Node(key, value));
        }
    }

    private void update(Node node) {
        delete(node);
        add(node);
    }

    private void delete(Node node) {
        Node ne = node.next;
        Node pr = node.prev;
        pr.next = ne;
        ne.prev = pr;
        m.remove(node.key);
    }

    private void add(Node node) {
        Node temp = head.next;
        head.next = node;
        node.prev = head;
        node.next = temp;
        temp.prev = node;
        m.put(node.key, node);
    }

    class Node{
        Node prev,next;
        int val, key;

        Node(int _key, int _val) {
            key = _key;
            val = _val;
        }

        Node() {}
    }
}
