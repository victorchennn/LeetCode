#include <iostream>
#include <string>

using namespace std;

class BreakPalindrome
{
public:
  string breakPalindrome(string palindrome)
  {
    int len = palindrome.size();
    for (int i{0}; i < len / 2; i++)
    {
      if (palindrome[i] != 'a')
      {
        palindrome[i] = 'a';
        return palindrome;
      }
    }
    palindrome[len - 1] = 'b';
    return len < 2 ? "" : palindrome;
  }
};