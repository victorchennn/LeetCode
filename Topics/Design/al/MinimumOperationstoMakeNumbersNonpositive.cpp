class Solution {
public:
    int minOperations(vector<int>& nums, int x, int y) {
        int left = 0;
        int right = *max_element(nums.begin(), nums.end());
        int firstTrueIndex = -1;

        auto feasible = [&](int time) {
            long long operationsNeeded = 0;
            for (int num : nums) {
                if (num > 1LL * time * y) { // base damage
                    long long remaining = num - 1LL * time * y; // what else needed?
                    operationsNeeded += (remaining + x - y - 1) / (x - y); // Ceiling division: (a + b - 1) / b
                    if (operationsNeeded > time) {
                        return false;
                    }
                }
            }
            return operationsNeeded <= time;
        };

        // Binary search template
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (feasible(mid)) {
                firstTrueIndex = mid;
                right = mid - 1;  // Try to find smaller valid value
            } else {
                left = mid + 1;
            }
        }

        return firstTrueIndex;
    }
};
