#include <iostream>
#include <string>
#include <stack>
#include <vector>

using namespace std;

class RemoveAllAdjacentDuplicatesInString
{
public:
  string removeDuplicates(string s, int k)
  {
    auto j{0};
    stack<int> counts;
    for (auto i{0}; i < s.size(); i++, j++)
    {
      s[j] = s[i];
      if (j == 0 || s[j] != s[j - 1])
      {
        counts.push(1);
      }
      else if (++counts.top() == k)
      {
        counts.pop();
        j -= k;
      }
    }
    return s.substr(0, j);
  }

  string removeDuplicatesII(string s, int k)
  {
    vector<pair<int, char>> counts;
    for (int i{0}; i < s.size(); i++)
    {
      if (counts.empty() || s[i] != counts.back().second)
      {
        counts.push_back({1, s[i]});
      }
      else if (++counts.back().first == k)
      {
        counts.pop_back();
      }
    }
    s = "";
    for (auto &p : counts)
    {
      s += string(p.first, p.second);
    }
    return s;
  }

  string removeDuplicatesIII(string s, int k)
  {
    stack<int> counts;
    for (int i{0}; i < s.size(); i++)
    {
      if (i == 0 || s[i] != s[i - 1])
      {
        counts.push(1);
      }
      else if (++counts.top() == k)
      {
        counts.pop();
        s.erase(i - k + 1, k);
        i -= k;
      }
    }
    return s;
  }
};