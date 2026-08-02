class Solution {
public:
    int binarySearchableNumbers(vector<int>& nums) {
        int n = nums.size();

        // Track which elements can be binary searchable
        vector<int> isBinarySearchable(n, 1);

        // First pass: left to right
        // An element cannot be binary searchable if there's a larger element to its left
        int maxSoFar = INT_MIN;
        for (int i = 0; i < n; ++i) {
            if (nums[i] < maxSoFar) {
                // Current element is smaller than some element on its left
                isBinarySearchable[i] = 0;
            }
            maxSoFar = max(maxSoFar, nums[i]);
        }

        // Second pass: right to left
        // An element cannot be binary searchable if there's a smaller element to its right
        // Also count the valid binary searchable numbers
        int minSoFar = INT_MAX;
        int count = 0;

        for (int i = n - 1; i >= 0; --i) {
            if (nums[i] > minSoFar) {
                // Current element is larger than some element on its right
                isBinarySearchable[i] = 0;
            }
            minSoFar = min(minSoFar, nums[i]);

            // Add to count if this element is binary searchable
            count += isBinarySearchable[i];
        }

        return count;
    }
};
