package Companies.Bloomberg;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordBreak {
    bool wordBreak(const string& s, const vector<string>& wordDict) {
        unordered_set<string> words(wordDict.begin(), wordDict.end());

        vector<bool> dp(s.size() + 1, false);
        dp[0] = true;

        for (int i = 1; i <= static_cast<int>(s.size()); ++i) {
            for (int j = 0; j < i; ++j) {
                if (dp[j] &&
                    words.find(s.substr(j, i - j)) != words.end()) {
                    dp[i] = true;
                    break;
                }
            }
        }

        return dp[s.size()];

        unordered_map<int, bool> memo;
        return dfs(s, 0, wordDict, memo);
    }

    /**
     * Time complexity: O(n^2). Size of recursion tree can go up to O(n^2).
     * Space complexity: O(n). The depth of recursion tree can go up to O(n).
     */
    bool dfs(const string& s,
             int start,
             const vector<string>& wordDict,
             unordered_map<int, bool>& memo) {
        if (start == static_cast<int>(s.size())) {
            return true;
        }

        if (auto it = memo.find(start); it != memo.end()) {
            return it->second;
        }

        for (const string& word : wordDict) {
            if (start + static_cast<int>(word.size()) >
                static_cast<int>(s.size())) {
                continue;
            }

            if (s.compare(start, word.size(), word) == 0 &&
                dfs(s,
                    start + static_cast<int>(word.size()),
                    wordDict,
                    memo)) {
                return memo[start] = true;
            }
        }

        return memo[start] = false;
    }
}
