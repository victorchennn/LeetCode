class FindPeakElement {
public:
    int findPeakElement(const std::vector<int>& nums) {
        int left = 0;
        int right = static_cast<int>(nums.size()) - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] < nums[mid + 1]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }
};

int findPeakElementRecursive(const std::vector<int>& nums) {
    return helper(nums, 0, static_cast<int>(nums.size()) - 1);
}

int helper(const std::vector<int>& nums, int left, int right) {
    if (left == right) {
        return left;
    }

    int mid = left + (right - left) / 2;

    if (nums[mid] > nums[mid + 1]) {
        return helper(nums, left, mid);
    }

    return helper(nums, mid + 1, right);
}
