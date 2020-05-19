package Companies.Microsoft;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AlienDictionary {
    public String alienOrder(String[] words) {
        if (words == null || words.length == 0) return "";
        Map<Character, Set<Character>> m = new HashMap<>();
        for (String word : words) {
            for (char c : word.toCharArray()) {
                m.putIfAbsent(c, new HashSet<>());
            }
        }
        for (int i = 0; i < words.length-1; i++) {
            int j = 0;
            while (j < words[i].length() && j < words[i+1].length()) {
                char a = words[i].charAt(j);
                char b = words[i+1].charAt(j);
                if (a != b) {
                    m.get(a).add(b);
                    break;
                }
                j++;
            }
            if (j < words[i].length() && j == words[i+1].length()) { // ["abc", "ab"]
                return "";
            }
        }
        StringBuilder sb = new StringBuilder();
        int[] visited = new int[26];
        for (char c : m.keySet()) {
            if (visited[c-'a'] == 0) {
                if (helper(c, sb, m, visited) == -1) {
                    return "";
                }
            }
        }
        return sb.toString();
    }

    private int helper(char c, StringBuilder sb, Map<Character, Set<Character>> m, int[] visited) {
        if (visited[c-'a'] != 0) {
            return visited[c-'a'];
        }
        visited[c-'a'] = -1;
        for (char next : m.get(c)) {
            if (helper(next, sb, m, visited) == -1) {
                return -1;
            }
        }
        visited[c-'a'] = 1;
        sb.insert(0, c);
        return 1;
    }
}
