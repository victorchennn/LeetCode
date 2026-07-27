#include <algorithm>
#include <vector>

class Solution {
public:
    void nextPermutation(std::vector<int>& nums) {
        if (nums.size() <= 1) {
            return;
        }

        int i = static_cast<int>(nums.size()) - 2;

        // 找到第一个 nums[i] < nums[i + 1] 的位置
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            --i;
        }

        if (i >= 0) {
            int j = static_cast<int>(nums.size()) - 1;

            // 从右边找第一个比 nums[i] 大的数
            while (nums[j] <= nums[i]) {
                --j;
            }

            std::swap(nums[i], nums[j]);
        }

        // 后缀原本是降序，翻转后变成升序
        std::reverse(nums.begin() + i + 1, nums.end());
    }
};

void testNextPermutation() {
    Solution solution;

    {
        std::vector<int> nums{1, 2, 3};
        solution.nextPermutation(nums);
        assert((nums == std::vector<int>{1, 3, 2}));
    }

    {
        std::vector<int> nums{3, 2, 1};
        solution.nextPermutation(nums);
        assert((nums == std::vector<int>{1, 2, 3}));
    }

    {
        std::vector<int> nums{1, 1, 5};
        solution.nextPermutation(nums);
        assert((nums == std::vector<int>{1, 5, 1}));
    }

    {
        std::vector<int> nums{1, 2, 7, 4, 3, 1};
        solution.nextPermutation(nums);
        assert((nums == std::vector<int>{1, 3, 1, 2, 4, 7}));
    }

    {
        std::vector<int> nums{1};
        solution.nextPermutation(nums);
        assert((nums == std::vector<int>{1}));
    }
}
