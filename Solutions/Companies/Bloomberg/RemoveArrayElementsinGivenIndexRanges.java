package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see AvailableTime
 * @see EmployeeFreeTime
 * @see MaximumLengthofPairChain
 * @see MeetingRooms
 * @see MergeIntervals
 * @see NonoverlappingIntervals
 */
public class RemoveArrayElementsinGivenIndexRanges {
    public int[] remove(int[] nums, int[][] intervals) {
        Arrays.sort(intervals, (a,b)->a[0]-b[0]);
        List<int[]> l = new ArrayList<>();
        int[] cur = intervals[0];
        l.add(cur);
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] > cur[1]) {
                cur = intervals[i];
                l.add(cur);
            } else {
                cur[1] = Math.max(cur[1], intervals[i][1]);
            }
        }
        List<Integer> re = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (!inRange(l, i)) {
                re.add(nums[i]);
            }
        }
        return re.stream().mapToInt(Integer::intValue).toArray();
    }

    private boolean inRange(List<int[]> l, int num) {
        for (int[] range : l) {
            if (num >= range[0] && num < range[1]) {
                return true;
            }
        }
        return false;
    }

    @Test
    void test() {
        int[] input = {-8, 3, -5, 1, 51, 56, 0, -5, 29, 43, 78, 75, 32, 76, 73, 76};
        int[][] ranges = {{5, 8}, {10, 13}, {3, 6}, {20, 25}};
        assertEquals(true, Arrays.equals(
                new int[]{-8,3,-5,29,43,76,73,76}, remove(input, ranges)));
    }
}
