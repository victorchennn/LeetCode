#include <iostream>
#include <vector>
#include <bitset>

using namespace std;

/**
 * num = 5, 000000100001
 * We can achieve 0 and 5 with [ ] and [ 5 ]
 * num = 2, 000010100101
 * We can achieve 0,2,5,7 from [5,2] ==> [ ], [5], [2], [5,2]
 * num = 4, 101011110101
 * We can achieve 0,2,4,5,6,7,11 from [5,2] ==> [ ], [5], [2], [4], [5,2], [2,4], [5,4], [5,2,4]
 */
class PartitionEqualSubsetSum
{
public:
  bool canPartition(vector<int> &nums)
  {
    bitset<10000> bits(1);
    int sum{0}; // accumulate(nums.begin(), nums.end(), 0)
    for (auto num : nums)
    {
      sum += num;
      bits |= bits << num;
    }
    return !(sum % 2) && bits[sum / 2];
  }
};