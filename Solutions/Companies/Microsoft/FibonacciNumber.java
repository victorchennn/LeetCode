package Companies.Microsoft;

public class FibonacciNumber {
    public int fib(int N) {
        if (N <= 1) {
            return N;
        }
        int first = 0, second = 1;
        for (int i = 2; i <= N; i++) {
            int third = first + second;
            first = second;
            second = third;
        }
        return second;
    }
}
