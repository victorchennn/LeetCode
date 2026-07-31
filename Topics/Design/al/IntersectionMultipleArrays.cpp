class Solution {
public:
    vector<int> intersection(vector<vector<int>>& nums) {
        vector<int> count(1001, 0);

        for (const auto& arr : nums) {
            for (int value : arr) {
                ++count[value];
            }
        }
    
        vector<int> result;
    
        for (int value = 1; value <= 1000; ++value) {
            if (count[value] == nums.size()) {
                result.push_back(value);
            }
        }
    
        return result;
    }
};

vector<int> intersection(vector<vector<int>>& nums) {
    unordered_map<int, int> count;

    for (const auto& arr : nums) {
        unordered_set<int> seen;

        for (int value : arr) {
            if (seen.insert(value).second) {
                ++count[value];
            }
        }
    }
  
    vector<int> result;
  
    for (const auto& [value, frequency] : count) {
        if (frequency == nums.size()) {
            result.push_back(value);
        }
    }

    sort(result.begin(), result.end());
    return result;
}
