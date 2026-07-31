#include <vector>

using namespace std;

class Solution {
public:
    int findKthLargest(vector<int>& nums, int k) {
        int left = 0;
        int right = static_cast<int>(nums.size()) - 1;

        // 第 k 大 = 升序排列后下标 n - k
        int target = static_cast<int>(nums.size()) - k;

        while (left < right) {
            int pivotIndex = partition(nums, left, right);

            if (pivotIndex == target) {
                break;
            }

            if (pivotIndex > target) {
                right = pivotIndex - 1;
            } else {
                left = pivotIndex + 1;
            }
        }

        return nums[target];
    }

private:
    int partition(vector<int>& nums, int left, int right) {
        int pivot = nums[left];

        while (left < right) {
            while (left < right && nums[right] >= pivot) {
                --right;
            }
            nums[left] = nums[right];

            while (left < right && nums[left] <= pivot) {
                ++left;
            }
            nums[right] = nums[left];
        }

        nums[left] = pivot;
        return left;
    }
};
