package Topics.Design;

import java.util.HashMap;
import java.util.Map;

public class SparseVector {
    private Map<Integer, Integer> values;
    private int[] array;

    /**
     * Time complexity: O(n) for creating the Hash Map, O(L) for calculating the dot product.
     *
     * Space complexity: O(L) for creating the Hash Map, as we only store elements that are non-zero,
     * and O(1) for calculating the dot product.
     */
    SparseVector(int[] nums) {
        values = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                values.put(i, nums[i]);
            }
        }
    }

    /**
     * Time complexity: O(n) for both constructing the sparse vector and calculating the dot product.
     *
     * Space complexity: O(1) for constructing the sparse vector as we simply save a reference to
     * the input array and O(1) for calculating the dot product.
     */
//    SparseVector(int[] nums) {
//        array = nums;
//    }

    public int dotProduct(SparseVector vec) {
        int re = 0;
        for (int i : this.values.keySet()) {
            if (vec.values.containsKey(i)) {
                re += this.values.get(i) * vec.values.get(i);
            }
        }
        return re;
    }
}
