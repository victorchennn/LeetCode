package Companies.Bloomberg;

public class ShortestUnsortedContinuousSubarray {

    /**
     * Input: [2, 6, 4, 8, 10, 9, 15]
     * Output: 5
     * Explanation: You need to sort [6, 4, 8, 10, 9] in ascending order to make
     *              the whole array sorted in ascending order.
     */
    public int findUnsortedSubarray(int[] nums) {
        int len = nums.length;
        int l = 0, r = 0, min = nums[len-1], max = nums[0];
        for (int i = 1; i < len; i++) {
            max = Math.max(max, nums[i]);
            min = Math.min(min, nums[len-1-i]);
            if (nums[i] < max) {
                r = i;
            }
            if (nums[len-1-i] > min) {
                l = len-1-i;
            }
        }
        return r-l == 0?0:r-l+1;
    }
}
