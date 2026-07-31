class Solution {
public:
    vector<int> searchRange(vector<int>& nums, int target) {
        vector<int> res{-1, -1};

        int left = binarySearch(nums, target, true);
        if (left == nums.size() || nums[left] != target) {
            return res;
        }

        res[0] = left;
        res[1] = binarySearch(nums, target, false) - 1;

        return res;
    }

private:
    int binarySearch(const vector<int>& nums, int target, bool left) {
        int l = 0;
        int r = nums.size();   // [l, r)

        while (l < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] > target || (left && nums[mid] == target)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }
};

class Solution {
public:
    vector<int> searchRange(vector<int>& nums, int target) {
        auto left = lower_bound(nums.begin(), nums.end(), target);

        if (left == nums.end() || *left != target) {
            return {-1, -1};
        }

        auto right = upper_bound(nums.begin(), nums.end(), target);

        return {
            static_cast<int>(left - nums.begin()),
            static_cast<int>(right - nums.begin() - 1)
        };
    }
};
