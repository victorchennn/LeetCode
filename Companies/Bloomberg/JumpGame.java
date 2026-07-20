package Companies.Bloomberg;

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
    
    // Given an array of integers arr, you are initially positioned at the first index of the array.
    // In one step you can jump from index i to index:
    // i + 1 where: i + 1 < arr.length.
    // i - 1 where: i - 1 >= 0.
    // j where: arr[i] == arr[j] and i != j.
    // Return the minimum number of steps to reach the last index of the array.
    public int minJumps(int[] arr) {
        Map<Integer, List<Integer>> valueToIndicesMap = new HashMap<>();
        int arrayLength = arr.length;
      
        for (int index = 0; index < arrayLength; index++) {
            valueToIndicesMap.computeIfAbsent(arr[index], k -> new ArrayList<>()).add(index);
        }
      
        boolean[] visited = new boolean[arrayLength];
        Deque<Integer> queue = new ArrayDeque<>();
      
        queue.offer(0);
        visited[0] = true;
      
        for (int steps = 0; ; steps++) {
            int currentLevelSize = queue.size();
            for (int i = 0; i < currentLevelSize; i++) {
                int currentIndex = queue.poll();
              
                if (currentIndex == arrayLength - 1) {
                    return steps;
                }
              
                List<Integer> sameValueIndices = valueToIndicesMap.get(arr[currentIndex]);
                for (int nextIndex : sameValueIndices) {
                    if (!visited[nextIndex]) {
                        visited[nextIndex] = true;
                        queue.offer(nextIndex);
                    }
                }
              
                // Clear the list to avoid redundant checks in future iterations
                // This optimization prevents revisiting the same value group
                sameValueIndices.clear();
              
                for (int nextIndex : new int[] {currentIndex - 1, currentIndex + 1}) {
                    if (nextIndex >= 0 && nextIndex < arrayLength && !visited[nextIndex]) {
                        visited[nextIndex] = true;
                        queue.offer(nextIndex);
                    }
                }
            }
        }
    }
    
    // You are given a 0-indexed binary string s and two integers minJump and maxJump. In the beginning, you are standing at index 0, which is equal to '0'. 
    // You can move from index i to index j if the following conditions are fulfilled:
    
    // i + minJump <= j <= min(i + maxJump, s.length - 1), and
    // s[j] == '0'.
    
    // Return true if you can reach index s.length - 1 in s, or false otherwise.
    public boolean canReach(String s, int minJump, int maxJump) {
        boolean[] dp = new boolean[s.length()];
        dp[0] = true;
        int index = 0;
        for(int i=0;i<s.length();i++){
            if(dp[i]){
                for(int j= Math.max(index,i+minJump); j<= Math.min(i+maxJump,s.length()-1); j++){
                    if(s.charAt(j)=='0') dp[j] = true; 
                }
                index = Math.min(i+maxJump,s.length()-1);
            }
        }
        return dp[dp.length-1];
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
