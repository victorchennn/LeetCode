package Topics.Design;

import java.util.*;

public class AutocompleteSystem {
    private Trie root;
    private String pre;

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
        List<String> re = new ArrayList<>();
        PriorityQueue<String> q = new PriorityQueue<>(cur.topK);
        while (!q.isEmpty()) {
            re.add(0, q.poll());
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
            cur.add(s, time);
        }
    }

    class Trie {
        Map<String, Integer> count;
        Map<Character, Trie> children;
        PriorityQueue<String> topK;
        Trie() {
            count = new HashMap<>();
            children = new HashMap<>();
            topK = new PriorityQueue<>((a,b)->!count.get(a).equals(count.get(b))?
                    count.get(a)-count.get(b):b.compareTo(a));
        }

        void add(String s, int time) {
            count.put(s, count.getOrDefault(s, 0)+time);
            if (topK.contains(s)) {
                topK.remove(s);
            }
            topK.add(s);
            if (topK.size() > 3) {
                topK.poll();
            }
        }

    }
}
