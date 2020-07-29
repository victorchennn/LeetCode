#include <iostream>
#include <string>

using namespace std;

class PalindromicSubstrings
{
public:
  int countSubstrings(string s)
  {
    int re{0}, len = s.size();
    for (int i = 0; i < len; i++)
    {
      for (int l = i, r = i; l >= 0 && r < len && s[l] == s[r]; l--, r++)
      {
        re++;
      }
      for (int l = i, r = i + 1; l >= 0 && r < len && s[l] == s[r]; l--, r++)
      {
        re++;
      }
    }
    return re;
  }
};