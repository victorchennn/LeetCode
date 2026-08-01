// 如果题目允许一个数字出现 3 次以上，就应该写得更稳健：
// nums[index] = -abs(nums[index]);

class Solution {
public:
    vector<int> findDuplicates(vector<int>& nums) {
        vector<int> result;

        for (int num : nums) {
            int index = abs(num) - 1;
            if (nums[index] < 0) {
                result.push_back(index + 1);
            }
            nums[index] = -nums[index];
        }

        return result;
    }
};

// 找所有缺失数字 最后扫描:
// 因为没访问过的位置一直保持正数。
for (int i = 0; i < nums.size(); i++) {
    if (nums[i] > 0)
        res.push_back(i + 1);
}

// 只有一个重复数字?
// Floyd Cycle Detection => Find_Duplicate_Number.cpp

// 找出现次数最多的数字?
// for (int x : nums)
//    nums[(x - 1) % n] += n;
