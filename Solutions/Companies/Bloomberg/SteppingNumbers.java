package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Find all the stepping numbers in range [n, m]. A number is called stepping number if all
 * adjacent digits have an absolute difference of 1.
 */
public class SteppingNumbers {
    public List<Integer> find(int l, int r) {
        List<Integer> re = new ArrayList<>();
        Deque<Integer> q = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        for (int i = 0; i <= 9; i++) {
            q.add(i);
            visited.add(i);
        }
        int last = 0, num1 = 0, num2 = 0;
        while (!q.isEmpty()) {
            int cur = q.poll();
            if (cur >= l && cur <= r) {
//                System.out.println(cur);
                re.add(cur);
            }
            if (cur == 0 || cur > r) {
                continue;
            }
            last = cur%10;
            num1 = cur*10 + last-1;
            num2 = cur*10 + last+1;
            if (!visited.contains(num1) && num1 <= r && last > 0) {
                q.add(num1);
            }
            if (!visited.contains(num2) && num2 <= r && last < 9) {
                q.add(num2);
            }
        }
        return re;
    }

    @Test
    void test() {
        assertEquals(13, find(0, 21).size());
        assertEquals(2, find(10, 15).size());
    }

}
