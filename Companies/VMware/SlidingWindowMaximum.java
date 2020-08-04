package Companies.VMware;

public class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0) {
            return new int[]{};
        }
        int[] re = new int[nums.length-k+1];
        int i = 0, maxp = 0, max = Integer.MIN_VALUE;
        while (i < k) {
            if (nums[i] >= max) {
                max = nums[i];
                maxp = i;
            }
            i++;
        }
        re[0] = max;
        while (i < nums.length) {
            if (nums[i] >= max) {
                max = nums[i];
                maxp = i;
            } else {
                if (maxp < i-k+1) {
                    max = nums[i];
                    maxp = i;
                    for (int j = i-1; j >= i-k+1; j--) {
                        if (nums[j] >= max) {
                            max = nums[j];
                            maxp = j;
                        }
                    }
                }
            }
            re[i-k+1] = max;
            i++;
        }
        return re;
    }
}
