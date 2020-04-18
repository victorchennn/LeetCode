package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class FindAllNumbersDisappearedinArray {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> re = new ArrayList<>();
        for (int num : nums) {
            int index = Math.abs(num)-1;
            if (nums[index] > 0) {
                nums[index] = -nums[index];
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                re.add(i+1);
            }
        }
        return re;
    }
}
