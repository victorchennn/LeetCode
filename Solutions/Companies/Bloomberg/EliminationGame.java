package Companies.Bloomberg;

import java.util.*;

public class EliminationGame {

    /**
     *  Input: n = 9
     *  1 2 3 4 5 6 7 8 9 (10)
     *  2 4 6 8 (10)
     *  2 6
     *  6
     *
     *  Output: 6
     */
    public int lastRemaining(int n) {
        boolean left = true;
        int head = 1, step = 1;
        while (n > 1) {
            if (left || n%2 == 1) {
                head += step;
            }
            step *= 2;
            n /= 2;
            left = !left;
        }
        return head;
    }

    /**
     *  Josephus problem
     *  Input: n = 9, k = 2
     *  1 2 3 4 5 6 7 8 9
     *  2 4 6 8 1 5 9 7 3
     *
     *  Output: 3
     */
    public int josephus(int n, int k) {
        if (n == 1) {
            return 1;
        } else {
          /* The position returned by josephus(n - 1, k)
            is adjusted because the recursive call
            josephus(n - 1, k) considers the original
            position k%n + 1 as position 1 */
            return (josephus(n - 1, k) + k-1) % n +1;
        }
    }

    public static void main(String...args) {
//        EliminationGame test = new EliminationGame();
//        System.out.print(test.josephus(9, 2));
//        System.out.print(test.josephus(6, 2));
    }
}
