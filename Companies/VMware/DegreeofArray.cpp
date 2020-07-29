#include <iostream>
#include <vector>
#include <unordered_map>

using namespace std;

class DegreeofArray
{
public:
  int findShortestSubArray(vector<int> &nums)
  {
    unordered_map<int, int> count{}, first{};
    int re{0}, degree{0};
    for (int i{0}; i < nums.size(); i++)
    {
      if (first.count(nums[i]) == 0)
      {
        first[nums[i]] = i;
      }
      if (++count[nums[i]] > degree)
      {
        degree = count[nums[i]];
        re = i - first[nums[i]] + 1;
      }
      else if (count[nums[i]] == degree)
      {
        re = min(re, i - first[nums[i]] + 1);
      }
    }
    return re;
  }
};