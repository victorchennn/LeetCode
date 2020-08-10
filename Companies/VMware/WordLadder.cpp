#include <iostream>
#include <string>
#include <vector>
#include <unordered_set>
#include <queue>

using namespace std;

class WordLadder {
public:
    int ladderLength(string beginWord, string endWord, vector<string>& wordList) {
        unordered_set<string> dict(wordList.begin(), wordList.end());
        unordered_set<string> head{}, tail{}, *phead, *ptail;
        if (dict.find(endWord) == dict.end()) {
            return 0;
        }
        head.insert(beginWord);
        tail.insert(endWord);
        int step = 2, len = beginWord.size();
        while (!head.empty() && !tail.empty()) {
            phead = head.size() < tail.size()? &head:&tail;
            ptail = head.size() < tail.size()? &tail:&head;
            unordered_set<string> temp{};
            for (auto it = phead->begin(); it != phead->end(); it++) {
                string word = *it;
                for (int i = 0; i < len; i++) {
                    char c = word[i];
                    for (char ch = 'a'; ch <= 'z'; ch++) {
                        word[i] = ch;
                        if (ptail->find(word) != ptail->end()) {
                            return step;
                        }
                        if (dict.find(word) != dict.end()) {
                            temp.insert(word);
                            dict.erase(word);
                        }
                    }
                    word[i] = c;
                }
            }
            step++;
            phead->swap(temp);
        }
        return 0;
    }

    int ladderLengthII(string beginWord, string endWord, vector<string>& wordList) {
        unordered_set<string> dict(wordList.begin(), wordList.end());
        queue<string> q{};
        q.push(beginWord);
        int step = 1, len = beginWord.size();
        while (!q.empty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                string word = q.front();
                q.pop();
                if (word == endWord) {
                    return step;
                }
                dict.erase(word);
                for (int j = 0; j < len; j++) {
                    char c = word[j];
                    for (char ch = 'a'; ch <= 'z'; ch++) {
                        word[j] = ch;
                        if (dict.find(word) != dict.end()) {
                            q.push(word);
                        }
                    }
                    word[j] = c;
                }
            }
            step++;
        }
        return 0;
    }
};