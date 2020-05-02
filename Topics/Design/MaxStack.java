package Topics.Design;

import java.util.*;

/**
 * TreeMap:
 * Time Complexity: O(logN) for all operations except top() is O(1)
 * Space Complexity: O(N)
 *
 * TwoStacks:
 * Time Complexity: O(1) for all operations except popMax() is O(N)
 * Space Complexity: O(N)
 */
public class MaxStack {

    private Node head, tail;
    private TreeMap<Integer, LinkedList<Node>> map;

    public MaxStack() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;

        map = new TreeMap<>();
    }

    public void push(int x) {
        Node newone = new Node(x);
        newone.next = head.next;
        head.next.prev = newone;
        head.next = newone;
        newone.prev = head;
        if (!map.containsKey(x)) {
            map.put(x, new LinkedList<>());
        }
        map.get(x).add(newone);
    }

    public int pop() {
        Node node = head.next;
        remove(node);

        LinkedList<Node> list = map.get(node.value);
        list.removeLast();
        if (list.isEmpty()) {
            map.remove(node.value);
        }
        return node.value;
    }

    public int top() {
        return head.next.value;
    }

    public int peekMax() {
        return map.lastKey();
    }

    public int popMax() {
        int max = peekMax();
        LinkedList<Node> list = map.get(max);
        Node maxNode = list.removeLast();
        remove(maxNode);
        if (list.isEmpty()) {
            map.remove(max);
        }
        return max;
    }

    private void remove(Node node) {
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }

    class Node {
        Node prev, next;
        int value;
        Node(int value) {
            this.value = value;
        }

        Node() {}

    }


//    private Deque<Integer> stack;
//    private Deque<Integer> maxStack;
//
//    /** initialize your data structure here. */
//    public MaxStack() {
//        stack = new ArrayDeque<>();
//        maxStack = new ArrayDeque<>();
//    }
//
//    public void push(int x) {
//        stack.push(x);
//        if(maxStack.isEmpty() || x>=maxStack.peek()) {
//            maxStack.push(x);
//        } else {
//            maxStack.push(maxStack.peek());
//        }
//    }
//
//    public int pop() {
//        maxStack.pop();
//        return stack.pop();
//    }
//
//    public int top() {
//        return stack.peek();
//    }
//
//    public int peekMax() {
//        return maxStack.peek();
//    }
//
//    public int popMax() {
//        int max = peekMax();
//
//        Deque<Integer> temp = new ArrayDeque<>();
//        while(top() != max) {
//            temp.push(pop());
//        }
//        pop();
//
//        while(!temp.isEmpty())
//            push(temp.pop());
//
//        return max;
//    }
}
