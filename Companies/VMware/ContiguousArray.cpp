#include <iostream>
#include <vector>
#include <unordered_map>

using namespace std;

class ContiguousArray
{
public:
  int findMaxLength(vector<int> &nums)
  {
    unordered_map<int, int> m{};
    m[0] = -1;
    int maxlen{0}, sum{0};
    for (int i{0}; i < nums.size(); i++)
    {
      sum += nums[i] ? 1 : -1;
      if (m.find(sum) != m.end())
      {
        maxlen = max(maxlen, i - m[sum]);
      }
      else
      {
        m[sum] = i;
      }
    }
    return maxlen;
  }
};