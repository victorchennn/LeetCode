package Companies.Amazon;

public class SortColors {
    public void sortColors(int[] nums) {
        int l = 0, r = nums.length-1, p = 0;
        while (p <= r) {
            if (nums[p] == 0) {
                swap(nums, l, p);
                p++;
                l++;
            } else if (nums[p] == 2) {
                swap(nums, r, p);
                r--;
            } else {
                p++;
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
