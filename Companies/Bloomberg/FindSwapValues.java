package Companies.Bloomberg;

import java.util.HashSet;
import java.util.Set;

/**
 * Swap value to make two arrays sum equal
 */
public class FindSwapValues {
    public static int[] findSwapValues(int[] a, int[] b) {
        if (a.length > b.length) {
            return findSwapValues(b, a);
        }
        int sum1 = sum(a), sum2 = sum(b);
        if ((sum1 - sum2)%2 != 0) {
            return new int[]{-1, -1};
        }
        int target = (sum1 - sum2)/2;
        Set<Integer> set = new HashSet<>();
        for (int num : a) {
            set.add(num);
        }
        for (int num : b) {
            if (set.contains(num + target)) {
                System.out.println(num+target + " " + num);
                return new int[]{num+target, num};
            }
        }
        return new int[]{-1, -1};
    }

    private static int sum(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        return sum;
    }

    public static void main(String...args) {
        findSwapValues(new int[]{4,1,2,1,1,2}, new int[]{3,6,3,3});
        findSwapValues(new int[]{5,7,4,6}, new int[]{1,2,3,8});
    }
}
