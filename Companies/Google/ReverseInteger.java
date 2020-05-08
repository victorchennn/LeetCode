package Companies.Google;

public class ReverseInteger {
    public int reverse(int x) {
        int re = 0, digit = 0, next = 0;
        while (x != 0) {
            digit = x%10;
            next = re*10 + digit;
            if ((next-digit)/10 != re) {
                return 0;
            }
            re = next;
            x /= 10;
        }
        return re;
    }
}
