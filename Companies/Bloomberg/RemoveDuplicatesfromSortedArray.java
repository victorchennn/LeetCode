package Companies.Bloomberg;

public class RemoveDuplicatesfromSortedArray {
    public int removeDuplicates(int[] nums) {
        int i = 0;
        for (int num : nums) {
            if (i < 1 || num > nums[i-1]) {
                nums[i++] = num;
            }
        }
        return i;
    }

    /* duplicates appeared at most twice */
    public int removeDuplicatesII(int[] nums) {
        int i = 0;
        for (int num : nums) {
            if (i < 2 || num > nums[i-2]) {
                nums[i++] = num;
            }
        }
        return i;
    }
}
