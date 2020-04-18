package Companies.ByteDance;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {

    private Map<Integer, Node> m;
    private int cap;
    private Node head, tail;

    public LRUCache(int capacity) {
        head = new Node();
        tail = new Node();
        cap = capacity;
        m = new HashMap<>();
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (!m.containsKey(key)) {
            return -1;
        }
        Node n = m.get(key);
        update(n);
        return n.val;
    }

    public void put(int key, int value) {
        if (m.containsKey(key)) {
            Node n = m.get(key);
            n.val = value;            // update value
            update(n);
        } else {
            if (m.size() == cap) {
                delete(tail.prev);
            }
            Node newone = new Node(key, value);
            add(newone);
        }
    }

    private void update(Node n) {
        delete(n);
        add(n);
    }

    private void add(Node n) {
        Node temp = head.next;
        head.next = n;
        n.prev = head;
        n.next = temp;
        temp.prev = n;
        m.put(n.key, n);
    }

    private void delete(Node n) {
        Node ne = n.next;
        Node pr = n.prev;
        pr.next = ne;
        ne.prev = pr;
        m.remove(n.key);
    }

    class Node {
        Node prev, next;
        int key, val;

        Node(int _key, int _val) {
            key = _key;
            val = _val;
        }

        Node() {

        }
    }
}
