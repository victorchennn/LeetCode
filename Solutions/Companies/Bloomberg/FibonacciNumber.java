package Companies.Bloomberg;

public class FibonacciNumber {
    /* Fibonacci Number */
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

    public static void main(String...args) {
        FibonacciNumber test = new FibonacciNumber();
        int num1 = 0, num2 = 1;
        while (num2 < 15) {
            System.out.println(num2);
            int temp = num1+num2;
            num1 = num2;
            num2 = temp;
        }
    }
}
