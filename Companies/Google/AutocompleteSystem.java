package Companies.Google;

import java.util.*;

public class AutocompleteSystem {

    Trie root;
    String pre;

    public AutocompleteSystem(String[] sentences, int[] times) {
        root = new Trie();
        pre = "";
        for (int i = 0; i < sentences.length; i++) {
            add(sentences[i], times[i]);
        }
    }

    public List<String> input(char c) {
        if (c == '#') {
            add(pre, 1);
            pre = "";
            return new ArrayList<>();
        }
        pre += c;
        Trie cur = root;
        for (char cc : pre.toCharArray()) {
            if (cur.children.get(cc) == null) {
                return new ArrayList<>();
            }
            cur = cur.children.get(cc);
        }
        PriorityQueue<Map.Entry<String, Integer>> q = new PriorityQueue<>((a, b)->
                a.getValue()==b.getValue()?a.getKey().compareTo(b.getKey()): b.getValue()-a.getValue());
        q.addAll(cur.count.entrySet());
        List<String> re = new ArrayList<>();
        int k = 3;
        while (k > 0 && !q.isEmpty()) {
            re.add(q.poll().getKey());
            k--;
        }
        return re;
    }

    private void add(String s, int time) {
        Trie cur = root;
        for (char c : s.toCharArray()) {
            if (cur.children.get(c) == null) {
                cur.children.put(c, new Trie());
            }
            cur = cur.children.get(c);
            cur.count.put(s, cur.count.getOrDefault(s, 0)+time);
        }
    }

    class Trie {
        Map<String, Integer> count;
        Map<Character, Trie> children;
        Trie() {
            count = new HashMap<>();
            children = new HashMap<>();
        }
    }
}
