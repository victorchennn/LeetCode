package Companies.Bloomberg;

public class ClimbingStairs {
    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        int num1 = 1, num2 = 2;
        for (int i = 3; i <= n; i++) {
            int temp = num1 + num2;
            num1 = num2;
            num2 = temp;
        }
        return num2;
    }
}
