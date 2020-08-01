package Companies.VMware;

import java.util.ArrayList;
import java.util.List;

/**
 * @see Companies.Bloomberg.LinkedListCycle
 */
public class FindtheDuplicateNumber {
    public int findDuplicate(int[] nums) {
        int slow = nums[0], fast = nums[0];
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);
        int head = nums[0];
        while (head != slow) {
            head = nums[head];
            slow = nums[slow];
        }
        return head;
    }

    /* Warning: Modify the array */
    public int findDuplicateII(int[] nums) {
        for (int num : nums) {
            int index = Math.abs(num)-1;
            if (nums[index] < 0) {
                return index+1;
            }
            nums[index] = -nums[index];
        }
        return 0;
    }

    /* Find all duplicates */
    public List<Integer> findDuplicates(int[] nums) {
        List<Integer> l = new ArrayList<>();
        for (int num : nums) {
            int index = Math.abs(num)-1;
            if (nums[index] < 0) {
                l.add(index+1);
            }
            nums[index] = -nums[index];
        }
        return l;
    }
}
