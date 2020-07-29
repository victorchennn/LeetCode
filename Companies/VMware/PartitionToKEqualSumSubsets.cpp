#include <iostream>
#include <vector>

using namespace std;

class PartitionToKEqualSumSubsets
{
public:
  bool canPartitionKSubsets(vector<int> &nums, int k)
  {
    int sum{0};
    for (int num : nums)
    {
      sum += num;
    }
    if (k <= 0 || sum % k != 0)
    {
      return false;
    }
    vector<bool> marks(nums.size(), false);
    return dfs(nums, k, sum / k, 0, 0, marks);
  }

private:
  bool dfs(vector<int> &nums, int k, int target, int sum, int start, vector<bool> &marks)
  {
    if (k == 1)
    {
      return true;
    }
    if (target == sum)
    {
      return dfs(nums, k - 1, target, 0, 0, marks);
    }
    if (target < sum)
    {
      return false;
    }
    for (int i = start; i < nums.size(); i++)
    {
      if (!marks[i])
      {
        marks[i] = true;
        if (dfs(nums, k, target, sum + nums[i], i + 1, marks))
        {
          return true;
        }
        marks[i] = false;
      }
    }
    return false;
  }
};