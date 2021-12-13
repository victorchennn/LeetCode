package Companies.Amazon;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Given an array of integers arr, find the sum of min(b),
 * where b ranges over every (contiguous) subarray of arr.
 */
public class SumofSubarrayMinimums {
    public int sumSubarrayMins(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        Deque<int[]> stack = new LinkedList<>(); // use deque to implement stack
//        Stack<int[]> stack = new Stack<>();
        int re = 0, sum = 0;
        for (int num : arr) {
            int count = 1;
            while (!stack.isEmpty() && stack.peek()[0] > num) {
                int[] cur = stack.pop();
                count += cur[1];
                sum -= cur[0]*cur[1];
            }
            stack.push(new int[]{num, count}); // push to the head of deque
//            stack.add(new int[]{num, count});
            sum += count*num;
            re += sum;
            re %= 1000000007;
        }
        return re %= 1000000007;
    }

    @Test
    void test() {
        assertEquals(17, sumSubarrayMins(new int[]{3,1,2,4}));
        assertEquals(444, sumSubarrayMins(new int[]{11,81,94,43,3}));
    }
}
