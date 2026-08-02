// Input: nums = [10,9,2,5,3,7,101,18] strictly increasing 
// Output: 4
class LongestIncreasingSubsequence{
public:
  int lengthOfLIS(vector<int> &nums)
  {
    vector<int> dp{};
    for (int x : nums) {
        auto it = lower_bound(dp.begin(), dp.end(), x);
        if (it == dp.end()) {
            dp.push_back(x);
        } else {
            *it = x;
        }
    }
    return dp.size();
  }
};

// Longest Decreasing Subsequence 把所有数取反，再跑 LIS。 x = -x;

// 如何输出真正的 LIS
// parent[i]：nums[i] 在 LIS 中前一个元素的下标
// tailIndex[len]：长度为 len + 1 的递增子序列，其最小结尾对应的原数组下标
vector<int> getLIS(const vector<int>& nums) {
    int n = nums.size();
    if (n == 0) return {};

    vector<int> tails;          // 只用于二分，存结尾值
    vector<int> tailIndex;      // tailIndex[len]：该长度结尾的原下标
    vector<int> parent(n, -1);  // parent[i]：i 前面的元素下标

    for (int i = 0; i < n; ++i) {
        int pos = lower_bound(tails.begin(), tails.end(), nums[i]) - tails.begin();
        if (pos == tails.size()) {
            tails.push_back(nums[i]);
            tailIndex.push_back(i);
        } else {
            tails[pos] = nums[i];
            tailIndex[pos] = i;
        }

        // nums[i] 接在长度为 pos 的子序列后面
        if (pos > 0) {
            parent[i] = tailIndex[pos - 1];
        }
    }

    // 从最长子序列最后一个元素开始回溯
    vector<int> lis;
    int cur = tailIndex.back();

    while (cur != -1) {
        lis.push_back(nums[cur]);
        cur = parent[cur];
    }

    reverse(lis.begin(), lis.end());
    return lis;
}
