package Companies.Amazon;

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

    private int helper(int n) {
        int sum = 0;
        while (n > 0) {
            sum += (n%10)*(n%10);
            n /= 10;
        }
        return sum;
    }
}
