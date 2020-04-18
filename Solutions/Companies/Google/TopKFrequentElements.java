package Companies.Google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopKFrequentElements {
    public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int i : nums) {
            m.put(i, m.getOrDefault(i, 0)+1);
        }
        List<Integer>[] l = new List[nums.length+1]; // Be careful
        for (int i : m.keySet()) {
            if (l[m.get(i)] == null) {
                l[m.get(i)] = new ArrayList<>();
            }
            l[m.get(i)].add(i);
        }
        List<Integer> re = new ArrayList<>();
        for (int i = l.length-1; i >= 0; i--) {
            if (l[i] != null && l[i].size() <= k) {
                re.addAll(l[i]);
                k -= l[i].size();
            }
        }
        return re;
    }
}
