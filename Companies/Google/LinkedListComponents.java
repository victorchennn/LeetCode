package Companies.Google;

import Libs.ListNode;

import java.util.HashSet;
import java.util.Set;

public class LinkedListComponents {
    public int numComponents(ListNode head, int[] G) {
        Set<Integer> s = new HashSet<>();
        for (int i : G) {
            s.add(i);
        }
        int re = 0;
        while (head != null) {
            if (s.contains(head.val) && (head.next == null || !s.contains(head.next.val))) {
                re++;
            }
            head = head.next;
        }
        return re;
    }
}
