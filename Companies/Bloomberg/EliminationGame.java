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
}
