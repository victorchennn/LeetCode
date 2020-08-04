#include <iostream>
#include <vector>

using namespace std;

class ProductofArrayExceptSelf
{
public:
  vector<int> productExceptSelf(vector<int> &nums)
  {
    vector<int> dp(nums.size(), 1);
    for (int i = 1; i < nums.size(); i++)
    {
      dp[i] = dp[i - 1] * nums[i - 1];
    }
    int temp = nums.back();
    for (int i = nums.size() - 2; i >= 0; i--)
    {
      dp[i] *= temp;
      temp *= nums[i];
    }
    return dp;
  }
};
