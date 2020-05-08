package Companies.Facebook;

import java.util.ArrayList;
import java.util.List;

/**
 * The set [1,2,3,...,n] contains a total of n! unique permutations.
 * By listing and labeling all of the permutations in order, we get the following sequence for n = 3:
 *
 * "123","132","213","231","312","321"
 *
 * Given n and k, return the kth permutation sequence.
 * Input: n = 3, k = 3 Output: "213"
 */
public class PermutationSequence {
    public String getPermutation(int n, int k) {
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            nums.add(i);
        }
        int[] facts = new int[n];
        facts[0] = 1;
        for (int i = 1; i < n; i++) {
            facts[i] = i*facts[i-1];
        }
        k -= 1;
        StringBuilder sb = new StringBuilder();
        for (int i = n; i > 0; i--) {
            int index = k/facts[i-1];
            k %= facts[i-1];
            sb.append(nums.get(index));
            nums.remove(index);
        }
        return sb.toString();
    }
}
