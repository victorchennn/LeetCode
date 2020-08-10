#include <iostream>
#include <vector>

using namespace std;

class FindPeakElement
{
public:
  int findPeakElement(vector<int> &nums)
  {
    int l = 0, r = nums.size() - 1;
    while (l < r)
    {
      int mid = (l + r) / 2;
      if (nums[mid] < nums[mid + 1])
        l = mid + 1;
      else
        r = mid;
    }
    return l;
  }

  int findPeakElementII(vector<int> &nums)
  {
    return helper(nums, 0, nums.size() - 1);
  }

  int findPeakElementIII(vector<int> &nums)
  {
    for (int i = 1; i < nums.size(); i++)
    {
      if (nums[i] < nums[i - 1])
        return i - 1;
    }
    return nums.size() - 1;
  }

private:
  int helper(const vector<int> &nums, int l, int r)
  {
    if (l == r)
      return l;
    else
    {
      int mid = (l + r) / 2;
      if (nums[mid] > nums[mid + 1])
        return helper(nums, l, mid);
      else
        return helper(nums, mid + 1, r);
    }
  }
};