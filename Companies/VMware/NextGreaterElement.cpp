#include <iostream>
#include <climits>
#include <string>
#include <algorithm>

using namespace std;

class NextGreaterElement
{
public:
  int nextGreaterElement(int n)
  {
    string num = to_string(n);
    if (!next(num)) // next_permutation(num.begin(), num.end());
    {
      return -1;
    }
    long long re = stoll(num);
    return re > INT_MAX ? -1 : re;
  }

private:
  bool next(string &num)
  {
    if (num.empty())
    {
      return false;
    }
    int i = num.size() - 2;
    while (i >= 0 && num[i] >= num[i + 1])
    {
      i--;
    }
    if (i == -1)
    {
      return false;
    }
    int j = num.size() - 1;
    while (num[j] <= num[i])
    {
      j--;
    }
    swap(num[i], num[j]);
    reverse(num.begin() + i + 1, num.end());
    return true;
  }
};