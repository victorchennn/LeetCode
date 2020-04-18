package Companies.Google;

public class HappyNumber {
    public boolean isHappy(int n) {
        int slow = n, fast = n;
        do {
            slow = helper(slow);
            fast = helper(helper(fast));
            if (fast == 1) {
                return true;
            }
        } while (slow != fast);
        return false;
    }

    private int helper(int num) {
        int re = 0;
        while (num > 0) {
            re += (num%10)*(num%10);
            num /= 10;
        }
        return re;
    }
}
