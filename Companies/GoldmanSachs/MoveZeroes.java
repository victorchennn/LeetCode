package Companies.GoldmanSachs;

public class MoveZeroes {
    /* Move zeros to right */
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

    /* Move zeros to left */
    public void moveZerosII(int[] nums) {
        int index = nums.length-1;
        for (int i = nums.length-1; i >= 0; i--) {
            if (nums[i] != 0) {
                int temp = nums[index];
                nums[index--] = nums[i];
                nums[i] = temp;
            }
        }
    }
}
