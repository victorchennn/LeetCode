package Companies.Google;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SentenceSimilarity {
    public boolean areSentencesSimilar(String[] words1, String[] words2, List<List<String>> pairs) {
        if (words1.length != words2.length) {
            return false;
        }
        Set<String> s = new HashSet<>();
        for (List<String> pair : pairs) {
            s.add(pair.get(0)+":"+pair.get(1));
        }
        for (int i = 0; i < words1.length; i++) {
            if (!words1[i].equals(words2[i])
                    && !s.contains(words1[i]+":"+words2[i])
                    && !s.contains(words2[i]+":"+words1[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean areSentencesSimilarTwo(String[] words1, String[] words2, List<List<String>> pairs) {
        if (words1.length != words2.length) {
            return false;
        }
        int count = 0;
        DSU dsu = new DSU(pairs.size() * 2);
        HashMap<String, Integer> m = new HashMap<>();
        for (List<String> pair : pairs) {
            for (String s : pair) {
                if (!m.containsKey(s)) {
                    m.put(s, count++);
                }
            }
            dsu.union(m.get(pair.get(0)), m.get(pair.get(1)));
        }
        for (int i = 0; i < words1.length; i++) {
            if (words1[i].equals(words2[i])) {
                continue;
            }
            if (!m.containsKey(words1[i]) || !m.containsKey(words2[i])
                    || dsu.find(m.get(words1[i])) != dsu.find(m.get(words2[i]))) {
                return false;
            }
        }
        return true;
    }

    class DSU {
        int[] parent;

        DSU(int N) {
            parent = new int[N];
            for (int i = 0; i < N; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            parent[find(x)] = find(y);
        }
    }
}
