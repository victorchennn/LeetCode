package Topics.Design;

import java.util.Random;

public class ShuffleanArray {
    private int[] nums;
    private Random rand = new Random();

    public ShuffleanArray(int[] nums) {
        this.nums = nums;
    }

    /** Resets the array to its original configuration and return it. */
    public int[] reset() {
        return nums;
    }

    /** Returns random shuffling of the array. */
    public int[] shuffle() {
        int[] re = nums.clone();
        for (int i = 1; i < nums.length; i++) {
            int j = rand.nextInt(i+1);
            swap(re, j, i);
        }
        return re;
    }

    private void swap(int[] nums, int l, int r) {
        int temp = nums[r];
        nums[r] = nums[l];
        nums[l] = temp;
    }
}
