package Companies.Bloomberg;

import java.util.*;

public class KillProcess {
    public List<Integer> killProcess(List<Integer> pid, List<Integer> ppid, int kill) {
        Map<Integer, List<Integer>> m = new HashMap<>();
        for (int i = 0; i < pid.size(); i++) {
            m.computeIfAbsent(ppid.get(i), k->new ArrayList<>()).add(pid.get(i));
        }
        List<Integer> re = new ArrayList<>();
        Queue<Integer> q = new LinkedList<>();
        q.add(kill);
        while (!q.isEmpty()) {
            int cur = q.poll();
            re.add(cur);
            if (m.get(cur) != null) {
                q.addAll(m.get(cur));
            }
        }
        return re;



//        re.add(kill);
//        helper(m, re, kill);
    }

    private void helper(Map<Integer, List<Integer>> m, List<Integer> re, int kill) {
        if (m.containsKey(kill)) {
            for (int child : m.get(kill)) {
                re.add(child);
                helper(m, re, child);
            }
        }
    }
}
