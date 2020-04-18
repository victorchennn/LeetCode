package Companies.Microsoft;

import java.util.List;
import java.util.Map;

public class WordBreak {
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length()+1];
        for (int i = 1; i <= s.length(); i++) {
            if (wordDict.contains(s.substring(0, i))) {
                dp[i] = true;
            } else {
                for (int j = 0; j < i; j++) {
                    if (dp[j] && wordDict.contains(s.substring(j, i))) {
                        dp[i] = true;
                        break;
                    }
                }
            }
        }
        return dp[s.length()];

//        return dfs(s, wordDict, new HashMap<>());
    }

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
