package Companies.Facebook;

import java.util.*;

public class EvaluateDivision {
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> m = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            m.computeIfAbsent(equations.get(i).get(0), k->new HashMap<>()).put(equations.get(i).get(1), values[i]);
            m.computeIfAbsent(equations.get(i).get(1), k->new HashMap<>()).put(equations.get(i).get(0), 1 / values[i]);
        }
        double[] re = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            re[i] = dfs(m, queries.get(i).get(0), queries.get(i).get(1), 1, new HashSet<>());
        }
        return re;
    }

    private double dfs(Map<String, Map<String, Double>> m, String s, String target, double cur, Set<String> set) {
        if (!m.containsKey(s) || !set.add(s)) {
            return -1;
        }
        if (s.equals(target)) {
            return cur;
        }
        Map<String, Double> children = m.get(s);
        for (String next : children.keySet()) {
            double re = dfs(m, next, target, cur * children.get(next), set);
            if (re != -1) {
                return re;
            }
        }
        return -1;
    }
}
