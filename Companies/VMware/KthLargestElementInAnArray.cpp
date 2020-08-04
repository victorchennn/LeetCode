#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <set>

using namespace std;

int findKthLargest(vector<int> nums, int k)
{
  nth_element(nums.begin(), nums.begin() + k - 1, nums.end(), greater<int>());
  return nums[k - 1];
}

int findKthLargestII(vector<int> nums, int k)
{
  partial_sort(nums.begin(), nums.begin() + k, nums.end(), greater<int>());
  return nums[k - 1];
}

int findKthLargestIII(vector<int> nums, int k)
{
  priority_queue<int, vector<int>, greater<int>> q; // min-heap
  // priority_queue<int> q(nums.begin(), nums.end());  // max-heap

  for (int num : nums)
  {
    q.push(num);
    if (q.size() > k)
      q.pop();
  }

  // for (int i = 0; i < k - 1; i++)
  //   q.pop();

  return q.top();
}

int findKthLargestIV(vector<int> nums, int k)
{
  multiset<int> s; // min-heap
  // multiset<int, greater<int>> s(nums.begin(), nums.end()); // max-heap

  for (int num : nums)
  {
    s.insert(num);
    if (s.size() > k)
      s.erase(s.begin());
  }

  // for (int i = 0; i < k - 1; i++)
  //   s.erase(s.begin());

  return *s.begin();
}

int helper(vector<int> &nums, int l, int r)
{
  int p = nums[l];
  while (l < r)
  {
    while (l < r && nums[r] >= p)
    {
      r--;
    }
    nums[l] = nums[r];
    while (l < r && nums[l] <= p)
    {
      l++;
    }
    nums[r] = nums[l];
  }
  nums[l] = p;
  return l;
}

int findKthLargestV(vector<int> nums, int k)
{
  int l = 0, r = nums.size() - 1;
  k = nums.size() - k;
  while (l < r)
  {
    int mid = helper(nums, l, r);
    if (mid == k)
    {
      break;
    }
    if (mid > k)
    {
      r = mid - 1;
    }
    else
    {
      l = mid + 1;
    }
  }
  return nums[k];
}

int main()
{
  vector<int> nums1{3, 2, 1, 5, 6, 4};
  vector<int> nums2{3, 2, 3, 1, 2, 4, 5, 5, 6};
  cout << findKthLargest(nums1, 2) << endl; // 5
  cout << findKthLargest(nums2, 4) << endl; // 4

  cout << findKthLargestII(nums1, 2) << endl; // 5
  cout << findKthLargestII(nums2, 4) << endl; // 4

  cout << findKthLargestIII(nums1, 2) << endl; // 5
  cout << findKthLargestIII(nums2, 4) << endl; // 4

  cout << findKthLargestIV(nums1, 2) << endl; // 5
  cout << findKthLargestIV(nums2, 4) << endl; // 4

  cout << findKthLargestV(nums1, 2) << endl; // 5
  cout << findKthLargestV(nums2, 4) << endl; // 4

  return 0;
}
