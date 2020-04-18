package Companies.Microsoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopKFrequentElements {
    public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int num : nums) {
            m.put(num, m.getOrDefault(num, 0)+1);
        }
        List<Integer>[] l = new List[nums.length+1];
        for (int num : m.keySet()) {
            if (l[m.get(num)] == null) {
                l[m.get(num)] = new ArrayList<>();
            }
            l[m.get(num)].add(num);
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
