// Input: nums = [1,2,3,1]
// Output: 4
// Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
// Total amount you can rob = 1 + 3 = 4.
class HouseRobber {
public:
    int robI1(vector<int>& nums) {
        int cur = 0;
        int prev = 0;
        for (int num : nums) {
            int temp = cur;
            cur = max(num + prev, cur);
            prev = temp;
        }
        return cur;
    }

    int robI2(vector<int>& nums) {
        vector<int> memo(nums.size(), -1);
        return helper(memo, nums, nums.size() - 1);
    }

    // cycle
    int robII(vector<int>& nums) {
        if (nums.size() == 1) {
            return nums[0];
        }

        return max(
            helperII(nums, 0, nums.size() - 1), // [0, n-2]
            helperII(nums, 1, nums.size())      // [1, n-1]
        );
    }

private:
    int helper(vector<int>& memo, vector<int>& nums, int i) {
        if (i < 0) {
            return 0;
        }

        if (memo[i] != -1) {
            return memo[i];
        }

        memo[i] = max(
            helper(memo, nums, i - 2) + nums[i],
            helper(memo, nums, i - 1)
        );

        return memo[i];
    }

    // solve nums[l ... r-1]
    int helperII(vector<int>& nums, int l, int r) {
        int cur = 0;
        int prev = 0;

        for (int i = l; i < r; i++) {
            int temp = cur;
            cur = max(prev + nums[i], cur);
            prev = temp;
        }

        return cur;
    }
};
