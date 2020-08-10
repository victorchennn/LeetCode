package Companies.VMware;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordBreak {
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length()+1];
        dp[0] = true;
        Set<String> set = new HashSet<>(wordDict);
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && set.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];

//        return dfs(s, wordDict, new HashMap<>());
    }

    /**
     * Time complexity: O(n^2). Size of recursion tree can go up to O(n^2).
     * Space complexity: O(n). The depth of recursion tree can go up to O(n).
     */
    private boolean dfs(String s, List<String> l, Map<String, Boolean> m) {
        if (m.containsKey(s)) {
            return m.get(s);
        }
        boolean exist = false;
        for (String word : l) {
            if (s.startsWith(word)) {
                String after = s.substring(word.length());
                if (after.length() == 0) {
                    exist = true;
                } else {
                    exist = dfs(after, l, m);
                }
                if (exist) {
                    break;
                }
            }
        }
        m.put(s, exist);
        return exist;
    }
}
