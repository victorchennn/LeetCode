#include <iostream>
#include <string>
#include <stack>
#include <algorithm>

using namespace std;

class RemoveKDigits
{
public:
  string removeKdigits(string num, int k)
  {
    if (k == num.size())
    {
      return "0";
    }
    stack<char> s{};
    for (char c : num)
    {
      while (k && !s.empty() && s.top() > c)
      {
        s.pop();
        k--;
      }
      s.push(c);
    }
    while (k)
    {
      s.pop();
      k--;
    }
    string re{};
    while (!s.empty())
    {
      re += s.top();
      s.pop();
    }
    while (re.size() > 1 && re.back() == '0')
    {
      re.pop_back();
    }
    reverse(re.begin(), re.end());
    return re;
  }
};