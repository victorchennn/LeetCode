package Companies.Google;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordBreak {

    public boolean wordBreak(String s, List<String> wordDict) {
        return dfs(s, wordDict, new HashMap<>());
    }

    private boolean dfs(String s, List<String> wordDict, Map<String, Boolean> m) {
        if (m.containsKey(s)) {
            return m.get(s);
        }
        if (s.length() == 0) {
            return true;
        }
        boolean re = false;
        for (String word : wordDict) {
            if (s.startsWith(word)) {
                re = dfs(s.substring(word.length()), wordDict, m);
                if (re) {
                    break;
                }
            }
        }
        m.put(s, re);
        return re;
    }

    public boolean wordBreakII(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length()+1];
        for (int i = 1; i <= s.length(); i++) {
            if (wordDict.contains(s.substring(0, i))) {
                dp[i] = true;
            } else {
                for (int j = 0; j < i; j++) {
                    if (dp[j] && wordDict.contains(s.substring(j, i))) {
                        dp[i] = true;
                        if (dp[i]) {
                            break;
                        }
                    }
                }
            }
        }
        return dp[s.length()];
    }
}
