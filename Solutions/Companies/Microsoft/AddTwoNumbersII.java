package Companies.Microsoft;

import Libs.ListNode;

import java.util.Stack;

public class AddTwoNumbersII {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();
        while (l1 != null) {
            s1.push(l1.val);
            l1 = l1.next;
        }
        while (l2 != null) {
            s2.push(l2.val);
            l2 = l2.next;
        }
        int carry = 0;
        ListNode head = new ListNode(0);
        while (!s1.isEmpty() || !s2.isEmpty()) {
            int n1 = s1.isEmpty()? 0:s1.pop();
            int n2 = s2.isEmpty()? 0:s2.pop();
            int sum = n1 + n2 + carry;
            head.val = sum%10;
            ListNode t = new ListNode(0);
            t.next = head;
            head = t;
            carry = sum/10;
        }
        if (carry != 0) {
            head.val = carry;
        }
        return head.val == 0? head.next:head;
    }


}
