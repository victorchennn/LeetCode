package Companies.Amazon;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JumpGame {
    /* Return true if you can reach the last index, or false otherwise. */
    public boolean canJump(int[] nums) {
        int last = nums.length-1;
        for (int i = nums.length-1; i >= 0; i--) {
            if (i + nums[i] >= last) {
                last = i;
            }
        }
        return last == 0;
    }

    /* minimum number of jumps */
    public int jump(int[] nums) {
        int re = 0, currentJumpEnd = 0, farthest = 0;
        for (int i = 0; i < nums.length-1; i++) { // i < nums.length-1 !!
            farthest = Math.max(farthest, i + nums[i]);
            if (i == currentJumpEnd) {
                re++;
                currentJumpEnd = farthest;
            }
        }
        return re;
    }

    /**
     * When you are at index i, you can jump to i + arr[i] or i - arr[i],
     * check if you can reach to any index with value 0.
     */
    public boolean canReach(int[] arr, int start) {
        if (start >= 0 && start < arr.length && arr[start] < arr.length) {
            int jump = arr[start];
            arr[start] += arr.length; // make it impossible to reach
            return jump == 0 || canReach(arr, start+jump) || canReach(arr, start-jump);
        }
        return false;
    }

    /**
     * Your score is the sum of all nums[j] for each index j you visited in the array.
     */
    public int maxResult(int[] nums, int k) {
        int[] score = new int[nums.length];
        Deque<Integer> dq = new LinkedList<>();
        score[0] = nums[0];
        dq.addLast(0);
        for (int i = 1; i < nums.length; i++) {
            while (!dq.isEmpty() && dq.peekFirst() < i-k) {
                dq.pollFirst();
            }
            score[i] = score[dq.peekFirst()] + nums[i];
            while (!dq.isEmpty() && score[i] >= score[dq.peekLast()]) {
                dq.pollLast();
            }
            dq.addLast(i);
        }
        return score[nums.length-1];
    }

    @Test
    void test() {
        assertEquals(true, canJump(new int[]{2,3,1,1,4}));
        assertEquals(false, canJump(new int[]{3,2,1,0,4}));

        assertEquals(2, jump(new int[]{2,3,1,1,4}));
        assertEquals(2, jump(new int[]{2,3,0,1,4}));

        assertEquals(true, canReach(new int[]{4,2,3,0,3,1,2}, 5));
        assertEquals(true, canReach(new int[]{4,2,3,0,3,1,2}, 0));
        assertEquals(false, canReach(new int[]{3,0,2,1,2}, 2));

        assertEquals(7, maxResult(new int[]{1,-1,-2,4,-7,3}, 2));
        assertEquals(17, maxResult(new int[]{10,-5,-2,4,0,3}, 3));
        assertEquals(0, maxResult(new int[]{1,-5,-20,4,-1,3,-6,-3}, 2));
    }
}
