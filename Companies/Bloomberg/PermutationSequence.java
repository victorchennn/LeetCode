package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.List;

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
