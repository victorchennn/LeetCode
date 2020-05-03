package Topics.Node;

import Libs.ListNode;

public class RemoveLinkedListElements {
    public ListNode removeElements(ListNode head, int val) {
        ListNode re = new ListNode(0);
        re.next = head;
        ListNode cur = re;
        while (cur.next != null) {
            if (cur.next.val == val) {
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return re.next;
    }
}
