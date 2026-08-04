/**
 * O(M×N)
 * M is the length of words
 * N is the total number of words in the input word list.
 */

class WordLadder {
public:
    int ladderLength(string beginWord, string endWord, vector<string>& wordList) {
        unordered_set<string> words(wordList.begin(), wordList.end());
        if (words.find(endWord) == words.end()) {
            return 0;
        }

        queue<string> q;
        q.push(beginWord);

        int steps = 1;
        words.erase(beginWord);

        while (!q.empty()) { 
            int levelSize = static_cast<int>(q.size());
            for (int i = 0; i < levelSize; ++i) { 
                string current = q.front();
                q.pop();

                for (size_t position = 0; position < current.size(); ++position) {
                    char original = current[position];
                    for (char ch = 'a'; ch <= 'z'; ++ch) {
                        if (ch == original) {
                            continue;
                        }
                      
                        current[position] = ch;
                        if (current == endWord) {
                            return steps + 1;
                        }
                      
                        auto it = words.find(current);
                        if (it != words.end()) {
                            q.push(current);
                            words.erase(it);
                        }
                    }
                    current[position] = original;
                }
            }

            ++steps;
        }
        return 0;
    }
};

// Bidirectional BFS 
int ladderLength(string beginWord, string endWord, vector<string>& wordList) {
    unordered_set<string> words(wordList.begin(), wordList.end());
    if (words.find(endWord) == words.end()) {
        return 0;
    }

    if (beginWord == endWord) {
        return 1;
    }

    unordered_set<string> beginSet{beginWord};
    unordered_set<string> endSet{endWord};

    words.erase(beginWord);
    words.erase(endWord);

    int steps = 1;
    while (!beginSet.empty() && !endSet.empty()) {
        if (beginSet.size() > endSet.size()) {
            swap(beginSet, endSet);
        }

        unordered_set<string> nextSet;
        for (const string& word : beginSet) {
            string candidate = word;

            for (size_t position = 0; position < candidate.size(); ++position) {
                char original = candidate[position];
                for (char ch = 'a'; ch <= 'z'; ++ch) {
                    if (ch == original) {
                        continue;
                    }

                    candidate[position] = ch;

                    // The two BFS searches have met.
                    if (endSet.contains(candidate)) {
                        return steps + 1;
                    }

                    auto it = words.find(candidate);
                    if (it != words.end()) {
                        nextSet.insert(candidate);
                        words.erase(it);
                    }
                }
                candidate[position] = original;
            }
        }

        beginSet = move(nextSet);
        ++steps;
    }

    return 0;
}

// Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
// Output: [["hit","hot","dot","dog","cog"],["hit","hot","lot","log","cog"]]
class Solution {
private:
    vector<vector<string>> result;
    unordered_map<string, vector<string>> predecessors;

public:
    vector<vector<string>> findLadders(string beginWord, string endWord, vector<string>& wordList) {
        result.clear();
        predecessors.clear();

        unordered_set<string> wordSet(wordList.begin(), wordList.end());
        if (!wordSet.count(endWord)) {
            return {};
        }

        unordered_map<string, int> distance;
        queue<string> q;

        q.push(beginWord);
        distance[beginWord] = 0;
        wordSet.erase(beginWord);

        bool found = false;
        int step = 0;

        while (!q.empty() && !found) {
            int size = q.size();
            ++step;

            for (int i = 0; i < size; ++i) {
                string current = q.front();
                q.pop();

                string next = current;

                for (int pos = 0; pos < next.size(); ++pos) {
                    char original = next[pos];

                    for (char c = 'a'; c <= 'z'; ++c) {
                        if (c == original) {
                            continue;
                        }

                        next[pos] = c;

                        // 已经在相同最短层访问过
                        if (distance.count(next) &&
                            distance[next] == step) {
                            predecessors[next].push_back(current);
                            continue;
                        }

                        // 不在字典中，或者已在更早层访问
                        if (!wordSet.count(next)) {
                            continue;
                        }

                        predecessors[next].push_back(current);
                        distance[next] = step;

                        q.push(next);
                        wordSet.erase(next);

                        if (next == endWord) {
                            found = true;
                        }
                    }

                    next[pos] = original;
                }
            }
        }

        if (found) {
            vector<string> path{endWord};
            buildPaths(endWord, beginWord, path);
        }

        return result;
    }

private:
    void buildPaths(const string& current, const string& beginWord, vector<string>& path) {
        if (current == beginWord) {
            result.emplace_back(path.rbegin(), path.rend());
            return;
        }

        for (const string& prev : predecessors[current]) {
            path.push_back(prev);
            buildPaths(prev, beginWord, path);
            path.pop_back();
        }
    }
};
