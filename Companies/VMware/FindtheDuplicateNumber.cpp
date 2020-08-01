#include <iostream>
#include <vector>

using namespace std;

class FindtheDuplicateNumber
{
public:
  int findDuplicate(vector<int> &nums)
  {
    if (nums.size() <= 1)
    {
      return -1;
    }
    int slow{nums[0]}, fast{nums[0]};
    do
    {
      slow = nums[slow];
      fast = nums[nums[fast]];
    } while (slow != fast);
    int head = nums[0];
    while (head != slow)
    {
      head = nums[head];
      slow = nums[slow];
    }
    return head;
  }
};