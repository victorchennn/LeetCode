package Companies.Amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordBreakII {
    public List<String> wordBreak(String s, List<String> wordDict) {
        return dfs(s, wordDict, new HashMap<>());
    }

    private List<String> dfs(String s, List<String> wordDict, Map<String, List<String>> m) {
        if (m.containsKey(s)) {
            return m.get(s);
        }
        List<String> l = new ArrayList<>();
        for (String word : wordDict) {
            if (s.startsWith(word)) {
                String after = s.substring(word.length());
                if (after.length() == 0) {
                    l.add(word);
                } else {
                    for (String sub : dfs(after, wordDict, m)) {
                        l.add(word + " " + sub);
                    }
                }
            }
        }
        m.put(s, l);
        return l;
    }
}
