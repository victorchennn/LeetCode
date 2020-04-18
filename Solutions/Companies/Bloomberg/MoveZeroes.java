package Companies.Bloomberg;

public class MoveZeroes {
    public void moveZeroes(int[] nums) {
        int index = 0;
        // for (int num : nums) {
        //     if (num != 0) {
        //         nums[index++] = num;
        //     }
        // }
        // while (index < nums.length) {
        //     nums[index++] = 0;
        // }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                int temp = nums[index];
                nums[index++] = nums[i];
                nums[i] = temp;
            }
        }
    }
}
