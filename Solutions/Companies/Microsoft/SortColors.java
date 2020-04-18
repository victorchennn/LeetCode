package Companies.Microsoft;

public class SortColors {
    public void sortColors(int[] nums) {
        int i = 0, j = nums.length-1, cur = 0;
        while (cur <= j) {
            if (nums[cur] == 0) {
                swap(nums, cur, i);
                i++;
                cur++;
            } else if (nums[cur] == 2) { // no need to add cur
                swap(nums, cur, j);
                j--;
            } else {
                cur++;
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
