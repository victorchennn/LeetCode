package Companies.Microsoft;

import java.util.*;

public class AccountsMerge {
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, Integer> ids = new HashMap<>();
        Map<String, String> names = new HashMap<>();
        DSU dsu = new DSU(10001);
        int id = 0;
        for (List<String> account : accounts) {
            String name = "";
            for (String email : account) {
                if (name.length() == 0) {
                    name = email;
                    continue;
                }
                names.put(email, name);
                if (!ids.containsKey(email)) {
                    ids.put(email, id++);
                }
                dsu.union(ids.get(account.get(1)), ids.get(email));
            }
        }
        Map<Integer, List<String>> m = new HashMap<>();
        for (String email : ids.keySet()) {
            int i = dsu.find(ids.get(email));
            m.computeIfAbsent(i, k->new ArrayList<>()).add(email);
        }
        for (List<String> l : m.values()) {
            Collections.sort(l);
            l.add(0, names.get(l.get(0)));
        }
        return new ArrayList<>(m.values());
    }

    class DSU {
        int[] parents;

        DSU(int N) {
            parents = new int[N];
            for (int i = 0; i < N; i++) {
                parents[i] = i;
            }
        }

        int find(int x) {
            if (parents[x] != x) {
                parents[x] = find(parents[x]);
            }
            return parents[x];
        }

        void union(int x, int y) {
            parents[find(x)] = find(y);
        }
    }
}
