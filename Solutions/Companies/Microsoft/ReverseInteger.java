package Companies.Microsoft;

public class ReverseInteger {
    public int reverse(int x) {
        int re = 0;
        while (x != 0) {
            int carry = x%10;
            int next = re*10 + carry;
            if ((next-carry)/10 != re) {
                return 0;
            }
            re = next;
            x /= 10;
        }
        return re;
    }
}
