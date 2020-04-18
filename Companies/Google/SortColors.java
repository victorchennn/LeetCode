package Companies.Google;

public class SortColors {
    public void sortColors(int[] nums) {
        int i = 0, j = nums.length-1, cur = 0;
        while (cur <= j) {  // Be careful!
            if (nums[cur] == 0) {
                int temp = nums[cur];
                nums[cur] = nums[i];
                nums[i] = temp;
                i++;
                cur++;
            } else if (nums[cur] == 2) {
                int temp = nums[cur];
                nums[cur] = nums[j];
                nums[j] = temp;
                j--;   // Be careful, no need to add cur
            } else {
                cur++;
            }
        }
    }
}
