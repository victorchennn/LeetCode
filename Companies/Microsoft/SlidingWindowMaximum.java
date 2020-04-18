package Companies.Microsoft;

public class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0) {
            return new int[]{};
        }
        int[] win = new int[nums.length-k+1];
        int i = 0, max = Integer.MIN_VALUE, maxp = 0;
        while (i < k) {
            if (nums[i] >= max) {
                max = nums[i];
                maxp = i;
            }
            i++;
        }
        win[0] = max;
        while (i < nums.length) {
            int cur = nums[i];
            if (cur >= max) {
                max = cur;
                maxp = i;
            } else {
                if (maxp < i-k+1) {
                    max = cur;
                    maxp = i;
                    for (int j = i; j >= i-k+1; j--) {
                        if (nums[j] > max) {
                            max = nums[j];
                            maxp = j;
                        }
                    }
                }
            }
            win[i-k+1] = max;
            i++;
        }
        return win;
    }
}
