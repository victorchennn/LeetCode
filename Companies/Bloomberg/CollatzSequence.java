package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * n: odd -> 3n+1, even -> n/2
 * find number of steps to 1
 *
 * @see MinimumStepstoGenerateNumber
 */
public class CollatzSequence {
    public int printCollatz(int n) {
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

    public int printCollatzII(int n) {
        if (n == 1) {
            return 1;
        }
        return (n & 1) == 1? 1 + printCollatzII(3*n+1):1+printCollatzII(n/2);
    }

    @Test
    void test() {
        assertEquals(9, printCollatz(6));
        assertEquals(9, printCollatzII(6));
    }
}
