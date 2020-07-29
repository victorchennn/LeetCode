#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <unordered_map>

using namespace std;

class LongestStrChain
{
public:
  int longestStrChain(vector<string> &words)
  {
    sort(words.begin(), words.end(), [](string x, string y) { return x.length() < y.length(); });
    unordered_map<string, int> m{};
    int re{0};
    for (string word : words)
    {
      for (int i{0}; i < word.length(); i++)
      {
        m[word] = max(m[word], m[word.substr(0, i) + word.substr(i + 1)] + 1);
      }
      re = max(re, m[word]);
    }
    return re;
  }
};