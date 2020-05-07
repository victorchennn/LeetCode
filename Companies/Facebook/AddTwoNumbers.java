package Companies.Facebook;

import Libs.ListNode;

public class AddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode re = new ListNode(0);
        ListNode t = re;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int x = l1 == null?0:l1.val;
            int y = l2 == null?0:l2.val;
            int sum = x + y + carry;
            carry = sum/10;
            t.next = new ListNode(sum%10);
            t = t.next;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (carry != 0) {
            t.next = new ListNode(carry);
        }
        return re.next;
    }
}
