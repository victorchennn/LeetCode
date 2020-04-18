package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class FindAllDuplicatesinArray {
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
