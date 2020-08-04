#include <iostream>
#include <vector>
#include <climits>
#include <deque>

using namespace std;

class SlidingWindowMaximum
{
public:
  vector<int> maxSlidingWindow(vector<int> &nums, int k)
  {
    vector<int> re{};
    deque<int> dp{};
    for (int i = 0; i < nums.size(); i++)
    {
      while (!dp.empty() && dp.front() < i - k + 1)
      {
        dp.pop_front();
      }
      while (!dp.empty() && nums[dp.back()] < nums[i])
      {
        dp.pop_back();
      }
      dp.push_back(i);
      if (i >= k - 1)
      {
        re.push_back(nums[dp.front()]);
      }
    }
    return re;
  }
};