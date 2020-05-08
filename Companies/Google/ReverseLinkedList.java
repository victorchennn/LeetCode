package Companies.Google;

import Libs.ListNode;

public class ReverseLinkedList {
    /* Iterative */
    public ListNode reverseList(ListNode head) {
        ListNode re = null;
        while (head != null) {
            ListNode temp = head.next;
            head.next = re;
            re = head;
            head = temp;
        }
        return re;
    }

    /* Recursive */
    public ListNode reverseListII(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode p = reverseListII(head.next);
        head.next.next = head;
        head.next = null;
        return p;
    }

    /* Between m-n */
    public ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null) {
            return null;
        }
        ListNode re = new ListNode(0);
        re.next = head;
        ListNode pre = re;
        ListNode cur = pre.next;
        int i = 1;
        while (i < m) {
            pre = cur;
            cur = cur.next;
            i++;
        }
        ListNode node = pre;
        while (i <= n) {
            ListNode temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
            i++;
        }
        node.next.next = cur;
        node.next = pre;
        return re.next;
    }
}
