package Companies.ByteDance;

public class MaximumSwap {
    public int maximumSwap(int num) {
        int[] last = new int[10];
        char[] nums = Integer.toString(num).toCharArray();
        for (int i = 0; i < nums.length; i++) {
            last[nums[i] - '0'] = i;
        }
        for (int i = 0; i < nums.length; i++) {
            for (int j = 9; j > nums[i] - '0'; j--) {
                if (last[j] > i) {
                    char temp = nums[i];
                    nums[i] = nums[last[j]];
                    nums[last[j]] = temp;
                    return Integer.valueOf(new String(nums));
                }
            }
        }
        return num;
    }
}
