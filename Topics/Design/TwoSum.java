package Topics.Design;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Trade-off, more add or more find?
 * more add: hashmap;
 * more find: two set;
 */
public class TwoSum {
    private Set<Integer> sums = new HashSet<>();
    private Set<Integer> nums = new HashSet<>();

    private Map<Integer, Integer> m = new HashMap<>();

    public TwoSum() {

    }

    /** Add the number to an internal data structure.. */
    public void add(int number) {
        // for (int num : nums) {
        //     sums.add(num+number);
        // }
        // nums.add(number);

        m.put(number, m.getOrDefault(number, 0)+1);
    }

    /** Find if there exists any pair of numbers which sum is equal to the value. */
    public boolean find(int value) {
        // return sums.contains(value);

        for (int com : m.keySet()) {
            if (m.containsKey(value-com)) {
                if (com != value-com || m.get(com) > 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
