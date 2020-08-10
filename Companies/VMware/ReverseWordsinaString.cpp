#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

class ReverseWordsinaString
{
public:
  //  Input: "the sky is blue"
  //  Output: "blue is sky the"
  string reverseWords(string s)
  {
    int l = s.size() - 1, r = s.size() - 1;
    string re{};
    while (l >= 0)
    {
      while (r >= 0 && s[r] == ' ')
        r--;
      if (r < 0)
        break;
      l = r;
      while (l >= 0 && s[l] != ' ')
        l--;
      re += s.substr(l + 1, r - l) + ' ';
      r = --l;
    }
    if (re.size())
      re.pop_back();
    return re;
  }

  //  Input:  ["t","h","e"," ","s","k","y"," ","i","s"," ","b","l","u","e"]
  //  Output: ["b","l","u","e"," ","i","s"," ","s","k","y"," ","t","h","e"]
  void reverseWords(vector<char> &s)
  {
    reverse(s.begin(), s.end());
    for (int l = 0, r = 0; l < s.size(); l = r + 1)
    {
      for (r = l; r < s.size(); r++)
      {
        if (isspace(s[r]))
        {
          break;
        }
      }
      reverse(s.begin() + l, s.begin() + r);
    }
  }
};