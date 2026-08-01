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

设：
从起点到环入口距离为 a
环入口到第一次相遇距离为 b
环长为 L

第一次相遇时：slow 走了 a + b fast 走了 a + b + kL
又因为 fast 比 slow 多走了一倍：2(a+b)=a+b+kL
a = kL - b 也就是 L-b
