#include <iostream>
#include <vector>

using namespace std;

class MissingNumber
{
public:
  int missingNumber(vector<int> &nums)
  {
    int re = nums.size();
    for (int i = 0; i < nums.size(); i++)
    {
      re ^= i ^ nums[i];
    }
    return re;
  }

  int missingNumberII(vector<int> &nums)
  {
    int sum = (nums.size()) * (nums.size() + 1) / 2;
    for (auto num : nums)
    {
      sum -= num;
    }
    return sum;
  }
};