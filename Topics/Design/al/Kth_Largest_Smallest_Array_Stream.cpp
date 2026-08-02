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

// Input:
// ["KthLargest", "add", "add", "add", "add", "add"]
// [[3, [4, 5, 8, 2]], [3], [5], [10], [9], [4]]

// Output: [null, 4, 5, 5, 8, 8]
class KthLargest {
private:
    int k_;
    priority_queue<int, vector<int>, greater<int>> minHeap_;

public:
    KthLargest(int k, vector<int>& nums)
        : k_(k) {
        for (int num : nums) {
            add(num);
        }
    }

    int add(int val) {
        if (minHeap_.size() < k_) {
            minHeap_.push(val);
        } else if (val > minHeap_.top()) {
            minHeap_.pop();
            minHeap_.push(val);
        }

        return minHeap_.top();
    }
};
