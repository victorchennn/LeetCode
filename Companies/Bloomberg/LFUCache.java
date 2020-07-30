package Companies.Bloomberg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class LFUCache {

    private Node head;
    private int cap;
    private Map<Integer, Integer> dict; // store key-value
    private Map<Integer, Node> nodes; // store key-node

    /**
     * We make a node-list, head with freq0, node1 with freq1, node2 with freq2...
     * For each frequency node, we use a queue to store keys with that frequency, and obviously front keys
     * meaning less recent used, so we can easily poll them out and delete.
     */
    public LFUCache(int capacity) {
        cap = capacity;
        dict = new HashMap<>();
        nodes = new HashMap<>();
    }

    public int get(int key) {
        if (!dict.containsKey(key)) {
            return -1;
        }
        update(key);
        return dict.get(key);
    }

    public void put(int key, int value) {
        if (cap == 0) {
            return;
        }
        if (!dict.containsKey(key)) {
            if (dict.size() == cap) {
                removeHead();
            }
            addHead(key);
        }
        dict.put(key, value);
        update(key);
    }

    private void update(int key) {
        Node cur = nodes.get(key);
        int freq = cur.freq;
        cur.keys.remove(key);

        if (cur.next == null) {
            Node newone = new Node(freq+1);
            newone.keys.add(key);
            cur.next = newone;
            newone.prev = cur;
        } else if (cur.next.freq != freq+1) {
            Node newone = new Node(freq+1);
            newone.keys.add(key);
            Node nextone = cur.next;
            newone.next = nextone;
            nextone.prev = newone;
            cur.next = newone;
            newone.prev = cur;
        } else {
            cur.next.keys.add(key);
        }
        nodes.put(key, cur.next);
        if (cur.keys.size() == 0) {
            remove(cur);
        }
    }

    private void remove(Node node) {
        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
    }

    private void removeHead() {
        int key = head.keys.poll();
        dict.remove(key);
        nodes.remove(key);
        if (head.keys.size() == 0) {
            remove(head);
        }
    }

    private void addHead(int key) {
        Node newone = new Node(0);
        newone.keys.add(key);
        newone.next = head;
        if (head != null) {
            head.prev = newone;
        }
        nodes.put(key, newone);
        head = newone;
    }

    private class Node {
        int freq;
        Queue<Integer> keys;
        Node prev, next;

        Node(int _freq) {
            freq = _freq;
            keys = new LinkedList<>();
        }
    }
}
