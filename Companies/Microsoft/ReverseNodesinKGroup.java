package Companies.Microsoft;

import Libs.ListNode;

public class ReverseNodesinKGroup {
    public ListNode reverseKGroup(ListNode head, int k) {
        int count = 0;
        ListNode node = head;
        while (node != null && count != k) {
            count++;
            node = node.next;
        }
        if (k == count) {
            node = reverseKGroup(node, k);
            while (count-- > 0) {
                ListNode temp = head.next;
                head.next = node;
                node = head;
                head = temp;
            }
            head = node; // !
        }
        return head;
    }
}
