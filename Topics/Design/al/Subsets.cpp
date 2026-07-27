#include <vector>

class Solution {
public:
    std::vector<std::vector<int>> subsets(std::vector<int>& nums) {
        // sort(nums.begin(), nums.end());
      
        std::vector<std::vector<int>> result;
        std::vector<int> subset;

        dfs(nums, 0, subset, result);

        return result;
    }

private:
    void dfs(const std::vector<int>& nums,
             int start,
             std::vector<int>& subset,
             std::vector<std::vector<int>>& result) {

        result.push_back(subset);

        for (int i = start; i < nums.size(); ++i) {
            // if (i > start && nums[i] == nums[i - 1])
            //   continue;
          
            subset.push_back(nums[i]);      // choose
            dfs(nums, i + 1, subset, result);
            subset.pop_back();              // backtrack
        }
    }
};
