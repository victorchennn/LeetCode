package Companies.Bloomberg;

public class FindPivotIndex {
    public int pivotIndex(int[] nums) {
        int sum = 0, cur = 0;
        for (int num : nums) {
            sum += num;
        }
        for (int i = 0; i < nums.length; i++) {
            if (cur == sum-cur-nums[i]) {
                return i;
            }
            cur += nums[i];
        }
        return -1;
    }
}
