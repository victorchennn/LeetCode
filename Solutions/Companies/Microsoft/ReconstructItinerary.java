package Companies.Microsoft;

import java.util.*;

public class ReconstructItinerary {
    Map<String, PriorityQueue<String>> m;
    LinkedList<String> re;

    public List<String> findItinerary(List<List<String>> tickets) {
        re = new LinkedList<>();
        m = new HashMap<>();
        for (List<String> t : tickets) {
            m.computeIfAbsent(t.get(0), k->new PriorityQueue<>()).add(t.get(1));
        }
        dfs("JFK");
        return re;
    }

    private void dfs(String s) {
        PriorityQueue<String> q = m.get(s);
        while (q != null && !q.isEmpty()) {
            dfs(q.poll());
        }
        re.addFirst(s);
    }
}
