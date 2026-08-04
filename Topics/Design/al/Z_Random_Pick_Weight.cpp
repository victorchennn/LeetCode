class Solution {
public:
    vector<int> prefixSum;  // Stores cumulative sum of weights

    Solution(vector<int>& w) {
        int size = w.size();
        prefixSum.resize(size + 1);

        // Build prefix sum array where prefixSum[i+1] = sum of weights from 0 to i
        for (int i = 0; i < size; ++i) {
            prefixSum[i + 1] = prefixSum[i] + w[i];
        }
    }

    int pickIndex() {
        int arraySize = prefixSum.size();

        // Generate random number in range [1, totalWeight]
        int randomValue = 1 + rand() % prefixSum[arraySize - 1];

        // Binary search to find the smallest index where prefixSum[index] >= randomValue
        int left = 1;
        int right = arraySize - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;  // Avoid potential overflow

            if (prefixSum[mid] >= randomValue) {
                right = mid;  // Target could be at mid or to its left
            } else {
                left = mid + 1;  // Target must be to the right of mid
            }
        }

        // Return the actual index (subtract 1 due to 1-based indexing in prefixSum)
        return left - 1;
    }
};
