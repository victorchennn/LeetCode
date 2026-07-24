package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JumpGame {
    /* Return true if you can reach the last index, or false otherwise. */
    bool canJump(const std::vector<int>& nums) {
        int last = static_cast<int>(nums.size()) - 1;
        for (int i = last; i >= 0; --i) {
            if (i + nums[i] >= last) {
                last = i;
            }
        }
        return last == 0;
    }

    /* minimum number of jumps */
    int jump(const std::vector<int>& nums) {
        int jumps = 0;
        int currentJumpEnd = 0;
        int farthest = 0;

        for (int i = 0; i < static_cast<int>(nums.size()) - 1; ++i) {
            farthest = std::max(farthest, i + nums[i]);
            if (i == currentJumpEnd) {
                ++jumps;
                currentJumpEnd = farthest;
            }
        }
        return jumps;
    }

    /**
     * When you are at index i, you can jump to i + arr[i] or i - arr[i],
     * check if you can reach to any index with value 0.
     */
    bool canReach(std::vector<int> arr, int start) {
        return canReachDFS(arr, start);
    }

    bool canReachDFS(std::vector<int>& arr, int index) {
        if (index < 0 ||  index >= static_cast<int>(arr.size()) ||
            arr[index] >= static_cast<int>(arr.size())) {
            return false;
        }

        int jump = arr[index];
        if (jump == 0) {
            return true;
        }

        // Mark this index as visited.
        arr[index] += static_cast<int>(arr.size());

        return canReachDFS(arr, index + jump) || canReachDFS(arr, index - jump);
    }

    /**
     * Your score is the sum of all nums[j] for each index j you visited in the array.
     */
    int maxResult(const std::vector<int>& nums, int k) {
        int n = static_cast<int>(nums.size());

        std::vector<int> score(n);
        std::deque<int> deque;

        score[0] = nums[0];
        deque.push_back(0);

        for (int i = 1; i < n; ++i) {
            // Remove indices outside the valid window [i-k, i-1].
            while (!deque.empty() && deque.front() < i - k) {
                deque.pop_front();
            }

            score[i] = score[deque.front()] + nums[i];

            // Maintain decreasing score values.
            while (!deque.empty() && score[i] >= score[deque.back()]) {
                deque.pop_back();
            }

            deque.push_back(i);
        }

        return score[n - 1];
    }
    
    // Given an array of integers arr, you are initially positioned at the first index of the array.
    // In one step you can jump from index i to index:
    // i + 1 where: i + 1 < arr.length.
    // i - 1 where: i - 1 >= 0.
    // j where: arr[i] == arr[j] and i != j.
    // Return the minimum number of steps to reach the last index of the array.
    int minJumps(const std::vector<int>& arr) {
        int n = static_cast<int>(arr.size());
        if (n <= 1) {
            return 0;
        }

        std::unordered_map<int, std::vector<int>> valueToIndices;
        for (int i = 0; i < n; ++i) {
            valueToIndices[arr[i]].push_back(i);
        }

        std::vector<bool> visited(n, false);
        std::queue<int> queue;

        queue.push(0);
        visited[0] = true;

        int steps = 0;

        while (!queue.empty()) {
            int levelSize = static_cast<int>(queue.size());

            for (int i = 0; i < levelSize; ++i) {
                int currentIndex = queue.front();
                queue.pop();

                if (currentIndex == n - 1) {
                    return steps;
                }

                // Visit all indices with the same value.
                auto& sameValueIndices = valueToIndices[arr[currentIndex]];
                for (int nextIndex : sameValueIndices) {
                    if (!visited[nextIndex]) {
                        visited[nextIndex] = true;
                        queue.push(nextIndex);
                    }
                }

                /*
                 * Important optimization:
                 * each group of equal values is processed only once.
                 */
                sameValueIndices.clear();

                int left = currentIndex - 1;
                int right = currentIndex + 1;
                if (left >= 0 && !visited[left]) {
                    visited[left] = true;
                    queue.push(left);
                }
                if (right < n && !visited[right]) {
                    visited[right] = true;
                    queue.push(right);
                }
            }
            
            ++steps;
        }

        return -1;
    }
    
    // You are given a 0-indexed binary string s and two integers minJump and maxJump. In the beginning, you are standing at index 0, which is equal to '0'. 
    // You can move from index i to index j if the following conditions are fulfilled:
    
    // i + minJump <= j <= min(i + maxJump, s.length - 1), and
    // s[j] == '0'.
    
    // Return true if you can reach index s.length - 1 in s, or false otherwise.
    bool canReach(const std::string& s, int minJump, int maxJump) {
        int n = static_cast<int>(s.size());

        std::vector<bool> reachable(n, false);
        reachable[0] = true;

        // First index that has not been checked yet.
        int nextUncheckedIndex = 0;

        for (int i = 0; i < n; ++i) {
            if (!reachable[i]) {
                continue;
            }

            int start = std::max(nextUncheckedIndex, i + minJump);
            int end = std::min(i + maxJump, n - 1);

            for (int j = start; j <= end; ++j) {
                if (s[j] == '0') {
                    reachable[j] = true;
                }
            }
            nextUncheckedIndex = end + 1;
        }
        return reachable[n - 1];
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
