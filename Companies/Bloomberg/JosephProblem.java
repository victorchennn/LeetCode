package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *  Josephus problem
 *  Input: n = 9, k = 2, Output: 3
 *  1 2 3 4 5 6 7 8 9
 *  2->4->6->8->1->5->9->7->3
 *
 *  @see EliminationGame
 */
public class JosephProblem {
    private static int josephus(int n, int k) {
        if (n == 1) {
            return 1;
        } else {
          /* The position returned by josephus(n - 1, k)
            is adjusted because the recursive call
            josephus(n - 1, k) considers the original
            position k%n + 1 as position 1 */
            return (josephus(n - 1, k) + k-1) % n + 1;
        }
    }

    private static int josephusII(int n, int k) {
        List<Integer> l = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            l.add(i);
        }
        int iteration = n-1, killPos = k-1;
        while (iteration > 0) {
            l.remove(killPos); // index
            killPos += k-1;
            if (killPos > l.size()-1) {
                killPos %= l.size();
            }
            iteration--;
        }
        return l.get(0);
    }

    @Test
    void test() {
        assertEquals(3, josephus(9, 2));
        assertEquals(4, josephus(10, 3));
        assertEquals(3, josephus(5, 2));
        assertEquals(4, josephus(7, 3));

        assertEquals(3, josephusII(9, 2));
        assertEquals(4, josephusII(10, 3));
        assertEquals(3, josephusII(5, 2));
        assertEquals(4, josephusII(7, 3));
    }
}
