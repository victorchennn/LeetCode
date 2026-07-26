package Companies.Bloomberg;

#include <string>
#include <unordered_map>
#include <vector>

using namespace std;

class WordBreakII {
private:
    vector<string> dfs(
        const string& s,
        int start,
        const vector<string>& wordDict,
        unordered_map<int, vector<string>>& memo
    ) {
        if (auto it = memo.find(start); it != memo.end()) {
            return it->second;
        }

        vector<string> result;

        for (const string& word : wordDict) {
            int wordLength = static_cast<int>(word.size());

            if (start + wordLength > static_cast<int>(s.size())) {
                continue;
            }

            if (s.compare(start, wordLength, word) != 0) {
                continue;
            }

            int nextStart = start + wordLength;

            if (nextStart == static_cast<int>(s.size())) {
                result.push_back(word);
            } else {
                vector<string> suffixes =
                    dfs(s, nextStart, wordDict, memo);

                for (const string& suffix : suffixes) {
                    result.push_back(word + " " + suffix);
                }
            }
        }

        memo[start] = result;
        return result;
    }

public:
    vector<string> wordBreak(
        const string& s,
        const vector<string>& wordDict
    ) {
        unordered_map<int, vector<string>> memo;
        return dfs(s, 0, wordDict, memo);
    }
};
