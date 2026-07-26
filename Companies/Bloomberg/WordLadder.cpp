package Companies.Bloomberg;

import java.util.*;

/**
 * O(M×N)
 * M is the length of words
 * N is the total number of words in the input word list.
 */
#include <queue>
#include <string>
#include <unordered_set>
#include <vector>

using namespace std;

class WordLadder {
public:
    int ladderLength(string beginWord,
                     string endWord,
                     vector<string>& wordList) {
        unordered_set<string> words(wordList.begin(), wordList.end());

        if (!words.contains(endWord)) {
            return 0;
        }

        queue<string> q;
        q.push(beginWord);

        // beginWord 本身算第一个单词。
        int steps = 1;

        // 防止 beginWord 本身也出现在 wordList 中。
        words.erase(beginWord);

        while (!q.empty()) {
            int levelSize = static_cast<int>(q.size());

            for (int i = 0; i < levelSize; ++i) {
                string current = q.front();
                q.pop();

                for (int position = 0;
                     position < static_cast<int>(current.size());
                     ++position) {
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

                            // 加入队列时立刻删除，相当于标记 visited。
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
