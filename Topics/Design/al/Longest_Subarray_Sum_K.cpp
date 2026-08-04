class Solution {
public:
    int longestSubarraySumK(vector<int>& nums, int k) {
        unordered_map<long long, int> firstIndex;

        // Prefix sum 0 appears before the array starts.
        firstIndex[0] = -1;

        long long prefixSum = 0;
        int maxLength = 0;

        for (int i = 0; i < nums.size(); ++i) {
            prefixSum += nums[i];

            // prefixSum - previousPrefixSum = k
            if (firstIndex.count(prefixSum - k)) {
                maxLength = max(
                    maxLength,
                    i - firstIndex[prefixSum - k]
                );
            }

            // Keep only the earliest occurrence for maximum length.
            if (!firstIndex.count(prefixSum)) {
                firstIndex[prefixSum] = i;
            }
        }

        return maxLength;
    }
};
