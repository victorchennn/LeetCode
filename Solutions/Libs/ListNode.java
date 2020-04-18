package Libs;

public class ListNode {
    public int val;
    public ListNode next;
    public ListNode(int x) { val = x; }

    public void print() {
        System.out.print(this.val);
        ListNode temp = this.next;
        while(temp != null) {
            System.out.print("->" + temp.val);
            temp = temp.next;
        }
        System.out.println();
    }
}