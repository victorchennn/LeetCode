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
