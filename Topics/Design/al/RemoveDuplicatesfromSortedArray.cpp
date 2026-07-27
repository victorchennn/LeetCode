class Solution {
public:
    int removeDuplicates(std::vector<int>& nums) {
       // std::vector<int> nums = {5, 2, 4, 1};
       // std::sort(nums.begin(), nums.end()); // , std::greater<int>()

      // int nums[] = {5, 2, 4, 1};
      // std::sort(std::begin(nums), std::end(nums)); // , [](int a, int b) { return a > b; }
      
        int i = 0;

        for (int num : nums) {
            if (i < 1 || num > nums[i - 1]) {
                nums[i++] = num;
            }
        }

        return i;
    }

    int removeDuplicatesII(std::vector<int>& nums) {
        int i = 0;

        for (int num : nums) {
            if (i < 2 || num > nums[i - 2]) {
                nums[i++] = num;
            }
        }

        return i;
    }

    // public int removeDuplicates(int[] nums, int k) {
    //     int i = 0;
    
    //     for (int num : nums) {
    //         if (i < k || num > nums[i - k]) {
    //             nums[i++] = num;
    //         }
    //     }
    
    //     return i;
    // }
};

void quickSort(vector<int>& nums, int left, int right) {
    if (left >= right) return;

    int pivot = nums[left + (right - left) / 2];
    int i = left, j = right;

    while (i <= j) {
        while (nums[i] < pivot) i++;
        while (nums[j] > pivot) j--;

        if (i <= j) {
            swap(nums[i], nums[j]);
            i++;
            j--;
        }
    }

    quickSort(nums, left, j);
    quickSort(nums, i, right);
}

// Random with duplicates
  {
      std::vector<int> nums{3,1,2,3,2,1};
      int k = sortAndRemoveDuplicates(nums);

      assert(k == 3);
      assert((std::vector<int>(nums.begin(), nums.begin()+k)
              == std::vector<int>{1,2,3}));
  }

// vector<int> nums = {5, 2, 4, 1, 3};
// quickSort(nums, 0, nums.size() - 1);
