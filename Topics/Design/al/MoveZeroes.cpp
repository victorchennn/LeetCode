#include <vector>
#include <utility>

class Solution {
public:
    void moveZeroes(std::vector<int>& nums) {
        int left = 0;

        for (int right = 0; right < nums.size(); ++right) {
            if (nums[right] != 0) {
                if (left != right) {
                    std::swap(nums[left], nums[right]);
                }
                ++left;
            }
        }
    }
};

int insertPos = 0;

for (int num : nums) {
    if (num != 0) {
        nums[insertPos++] = num;
    }
}

while (insertPos < nums.size()) {
    nums[insertPos++] = 0;
}
