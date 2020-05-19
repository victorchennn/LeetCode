package Companies.GoldmanSachs;

import java.util.*;

/**
 * @see Companies.Bloomberg.AllPathsFromSourcetoTarget
 */
public class ReconstructItinerary {
    public List<String> findItinerary(List<List<String>> tickets) {
        List<String> re = new ArrayList<>();
        Map<String, PriorityQueue<String>> m = new HashMap<>();
        for (List<String> t : tickets) {
            m.computeIfAbsent(t.get(0), k->new PriorityQueue<>()).add(t.get(1));
        }
        dfs("JFK", m, re);
        return re;
    }

    private void dfs(String s, Map<String, PriorityQueue<String>> m, List<String> re) {
        PriorityQueue<String> q = m.get(s);
        while (q != null && !q.isEmpty()) {
            dfs(q.poll(), m, re);
        }
        re.add(0, s);
    }
}
