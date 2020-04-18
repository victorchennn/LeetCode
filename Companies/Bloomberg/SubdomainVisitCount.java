package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubdomainVisitCount {
    public List<String> subdomainVisits(String[] cpdomains) {
        Map<String, Integer> m = new HashMap<>();
        for (String cp : cpdomains) {
            String[] pairs = cp.split("\\s+");
            int num = Integer.valueOf(pairs[0]);
            String[] frags = pairs[1].split("\\.");
            String cur = "";
            for (int i = frags.length - 1; i >= 0; i--) {
                cur = frags[i] + (i == frags.length-1?"":".") + cur;
                m.put(cur, m.getOrDefault(cur, 0)+num);
            }
        }
        List<String> re = new ArrayList<>();
        for (String key : m.keySet()) {
            re.add(m.get(key) + " " + key);
        }
        return re;
    }
}
