package Topics.Design;

public class DesignHashMap {
    ListNode[] l;
    int size;

    /** Initialize your data structure here. */
    public DesignHashMap() {
        l = new ListNode[1000];
        size = l.length;
    }

    /** value will always be non-negative. */
    public void put(int key, int value) {
        int code = code(key);
        if (l[code] == null) {
            l[code] = new ListNode(-1, -1);
        }
        ListNode prev = find(l[code], key);
        if (prev.next == null) {
            prev.next = new ListNode(key, value);
        } else {
            prev.next.val = value;
        }
    }

    /** Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key */
    public int get(int key) {
        int code = code(key);
        if (l[code] == null) {
            return -1;
        }
        ListNode prev = find(l[code], key);
        return prev.next == null? -1:prev.next.val;
    }

    /** Removes the mapping of the specified value key if this map contains Companies.Amazon mapping for the key */
    public void remove(int key) {
        int code = code(key);
        if (l[code] == null) {
            return;
        }
        ListNode prev = find(l[code], key);
        if (prev.next == null) {
            return;
        }
        prev.next = prev.next.next;
    }

    private ListNode find(ListNode root, int key) {
        ListNode cur = root, prev = null;
        while (cur != null && cur.key != key) {
            prev = cur;
            cur = cur.next;
        }
        return prev;
    }

    private int code(int key) {
        return Integer.hashCode(key)%size;
    }

    class ListNode {
        ListNode next;
        int key, val;
        ListNode(int _key, int _val) {
            key = _key;
            val = _val;
        }
    }
}
