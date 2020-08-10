#include <iostream>
#include <string>
#include <vector>
#include <set>
#include <unordered_map>
#include <cstring>

using namespace std;

class WordBreak {
public:
    bool wordBreak(string s, vector<string>& wordDict) {
        int len = s.size();
        set<string> dict{};
        for (auto w : wordDict) {
            dict.insert(w);
        }
        vector<bool> dp(len+1, false);
        dp[0] = true;
        for (int i = 1; i <= len; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.find(s.substr(j, i-j)) != dict.end()) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[len];
    }

    bool wordBreakII(string s, vector<string>& wordDict) {
        int len = s.size();
        unordered_map<string, bool> m{};
        for (auto w : wordDict) {
            m[w] = true;
        }
        bool dp[len+1];
        memset(dp, false, sizeof(dp));
        dp[len] = true;
        string cur{ "" };
        for (int i = len-1; i >= 0; i--) {
            cur = "";
            for (int j = i; j < len; j++) {
                cur += s[j];
                if (!dp[j+1] || m.count(cur) == 0) {
                    continue;
                }
                dp[i] = true;
                break;
            }
        }
        return dp[0];
    }
};
