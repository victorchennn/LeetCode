#include <vector>

class MajorityElement {
public:
    // Find the element that appears more than n / 2 times.
    int majorityElement(const std::vector<int>& nums) {
        int candidate = 0;
        int count = 0;
    
        for (int num : nums) {
            if (count == 0) {
                candidate = num;
            }
    
            count += (num == candidate) ? 1 : -1;
        }
    
        return candidate;
    }

    // Find all elements that appear more than floor(n / 3) times.
    std::vector<int> majorityElementII(const std::vector<int>& nums) {
        std::vector<int> result;

        if (nums.empty()) {
            return result;
        }

        int candidate1 = 0;
        int candidate2 = 1;
        int count1 = 0;
        int count2 = 0;

        // First pass: find at most two possible candidates.
        for (int num : nums) {
            if (num == candidate1) {
                ++count1;
            } else if (num == candidate2) {
                ++count2;
            } else if (count1 == 0) {
                candidate1 = num;
                count1 = 1;
            } else if (count2 == 0) {
                candidate2 = num;
                count2 = 1;
            } else {
                --count1;
                --count2;
            }
        }

        // Second pass: verify the candidates.
        count1 = 0;
        count2 = 0;

        for (int num : nums) {
            if (num == candidate1) {
                ++count1;
            } else if (num == candidate2) {
                ++count2;
            }
        }

        if (count1 > static_cast<int>(nums.size()) / 3) {
            result.push_back(candidate1);
        }

        if (count2 > static_cast<int>(nums.size()) / 3) {
            result.push_back(candidate2);
        }

        return result;
    }
};
