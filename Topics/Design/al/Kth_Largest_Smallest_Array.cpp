class Solution {
public:
    int findKthLargest(vector<int>& nums, int k) {
        int target = nums.size() - k;
        quickSelect(nums, 0, nums.size() - 1, target);
        return nums[target];
    }

    vector<int> kSmallest(vector<int>& nums, int k) {
        quickSelect(nums, 0, nums.size() - 1, k - 1);
        return vector<int>(nums.begin(), nums.begin() + k);
    }
    
    void quickSelect(vector<int>& nums, int l, int r, int k) {
        if (l >= r) return;
        int p = partition(nums, l, r);
        if (p == k)
            return;
        else if (p > k)
            quickSelect(nums, l, p - 1, k);
        else
            quickSelect(nums, p + 1, r, k);
    }

    int partition(vector<int>& nums, int l, int r) {
        int pivot = nums[r];
        int i = l;
    
        for (int j = l; j < r; j++) {
            if (nums[j] < pivot) {
                swap(nums[i++], nums[j]);
            }
        }
    
        swap(nums[i], nums[r]);
        return i;
    }
};
