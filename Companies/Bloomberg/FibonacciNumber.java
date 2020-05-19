package Companies.Bloomberg;

/**
 * @see ClimbingStairs
 */
public class FibonacciNumber {
    public int fib(int N) {
        if (N <= 1) {
            return N;
        }
        int num1 = 0, num2 = 1;
        for (int i = 2; i <= N; i++) {
            int temp = num1 + num2;
            num1 = num2;
            num2 = temp;
        }
        return num2;
    }
}
