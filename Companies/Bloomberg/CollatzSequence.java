package Companies.Bloomberg;

/**
 * n: odd -> 3n+1, even -> n/2
 * find number of steps to 1
 *
 * @see MinimumStepstoGenerateNumber
 */
public class CollatzSequence {
    public static int printCollatz(int n) {
        int step = 1;
        while (n != 1) {
            step++;
            System.out.print(n + "->");
            if ((n & 1) == 1) {
                n = 3*n + 1;
            } else {
                n /= 2;
            }
        }
        System.out.println(n);
        System.out.println("Step: " + step);
        return step;
    }

    public static int printCollatzII(int n) {
        if (n == 1) {
            return 1;
        }
        return (n & 1) == 1? 1 + printCollatzII(3*n+1):1+printCollatzII(n/2);
    }

    public static void main(String...args) {
        printCollatz(6); // 6, 3, 10, 5, 16, 8, 4, 2, 1
        printCollatz(3); // 3, 10, 5, 16, 8, 4, 2, 1
        System.out.println(printCollatzII(6));
        System.out.println(printCollatzII(3));
    }
}
