#include <iostream>
#include <string>

using namespace std;

class DecodeString
{
public:
  string decodeString(string s)
  {
    int i{0};
    return helper(s, i);
  }

  string helper(const string &s, int &i)
  {
    string re{};
    while (i < s.size() && s[i] != ']')
    {
      if (!isdigit(s[i]))
      {
        re += s[i++];
      }
      else
      {
        int num{0};
        while (i < s.size() && isdigit(s[i]))
        {
          num = num * 10 + s[i++] - '0';
        }
        i++; // '['
        string after = helper(s, i);
        i++; // ']'
        while (num-- > 0)
        {
          re += after;
        }
      }
    }
    return re;
  }
};