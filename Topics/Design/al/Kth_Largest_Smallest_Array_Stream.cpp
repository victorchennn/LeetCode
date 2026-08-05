class Solution {
public:
    int findKthLargest(vector<int>& nums, int k) {
        // if (nums.empty() || k <= 0 || k > nums.size()) {
        //     throw invalid_argument("Invalid k");
        // }
        int target = nums.size() - k;
        quickSelect(nums, 0, nums.size() - 1, target);
        return nums[target];
    }

    vector<int> kSmallest(vector<int>& nums, int k) {
        quickSelect(nums, 0, nums.size() - 1, k - 1);
        return vector<int>(nums.begin(), nums.begin() + k);
    }
    
    void quickSelect(vector<int>& nums, int l, int r, int target) {
        while (l < r) {
            int p = partition(nums, l, r);
            if (p == target) {
                return;
            }
            if (p > target) {
                r = p - 1;
            } else {
                l = p + 1;
            }
        }
    }

    // [l, i - 1] 里的值 < pivot, nums[i] == pivot, [i + 1, r] 里的值 >= pivot
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

    // void quickSort(vector<int>& nums, int l, int r) {
    //     if (l >= r)
    //         return;
    
    //     int p = partition(nums, l, r);
    
    //     quickSort(nums, l, p - 1);
    //     quickSort(nums, p + 1, r);
    // }
};

TEST(QuickSelectTest, FindKthLargest) {
    Solution solution;

    vector<int> nums1{3, 2, 1, 5, 6, 4};
    EXPECT_EQ(solution.findKthLargest(nums1, 2), 5);

    vector<int> nums2{3, 2, 3, 1, 2, 4, 5, 5, 6};
    EXPECT_EQ(solution.findKthLargest(nums2, 4), 4);

    vector<int> nums3{-5, -1, -10, -3};
    EXPECT_EQ(solution.findKthLargest(nums3, 2), -3);
}

TEST(QuickSelectTest, KSmallest) {
    Solution solution;

    vector<int> nums1{7, 2, 9, 1, 5};
    vector<int> result1 = solution.kSmallest(nums1, 3);
    sort(result1.begin(), result1.end());
    EXPECT_EQ(result1, vector<int>({1, 2, 5}));

    vector<int> nums2{3, 1, 2, 1, 4, 2};
    vector<int> result2 = solution.kSmallest(nums2, 4);
    sort(result2.begin(), result2.end());
    EXPECT_EQ(result2, vector<int>({1, 1, 2, 2}));

    vector<int> nums3{5, 3, 8, 1};
    vector<int> result3 = solution.kSmallest(nums3, 0);
    EXPECT_TRUE(result3.empty());
}

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
