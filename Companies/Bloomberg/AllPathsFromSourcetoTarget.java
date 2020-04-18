package Companies.Bloomberg;

import java.util.*;

public class AllPathsFromSourcetoTarget {
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        List<List<Integer>> re = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        cur.add(0);
        dfs(re, cur, 0, graph);
        return re;
    }

    private void dfs(List<List<Integer>> re, List<Integer> cur, int p, int[][] graph) {
        if (p == graph.length-1) {
            re.add(new ArrayList<>(cur));
            return;
        }
        for (int next : graph[p]) {
            cur.add(next);
            dfs(re, cur, next, graph);
            cur.remove(cur.size()-1);
        }

    }

    private static List<List<Character>> getPossibleRoutes(char[][] chars, char start, char end) {
        Map<Character, Set<Character>> map = new HashMap<>();
        for(char[] cs : chars) {
            map.putIfAbsent(cs[0], new HashSet<>());
            map.putIfAbsent(cs[1], new HashSet<>());
            map.get(cs[0]).add(cs[1]);
            map.get(cs[1]).add(cs[0]);
        }
        List<List<Character>> res = new ArrayList<>();
        Set<Character> visited = new HashSet<>();
        visited.add(start);
        List<Character> cur = new ArrayList<>();
        cur.add(start);
        dfs(map, visited, res, cur, start, end);
        return res;
    }

    private static void dfs(Map<Character, Set<Character>> map, Set<Character> visited, List<List<Character>> res, List<Character> l, char cur, char end) {
        if(cur == end) {
            res.add(new ArrayList<>(l));
            return;
        }
        for(char nei : map.getOrDefault(cur, new HashSet<>())) {
            if(!visited.contains(nei)) {
                l.add(nei);
                visited.add(nei);
                dfs(map, visited, res, l, nei, end);
                l.remove(l.size()-1);
                visited.remove(nei);
            }
        }
    }

    public static void main(String[] args) {
        char[][] chars = {{'A', 'B'}, {'A', 'C'}, {'A', 'D'}, {'B', 'C'}, {'B', 'D'}};
        char start = 'C', end = 'D';
        System.out.println(getPossibleRoutes(chars, start, end));
    }
}
