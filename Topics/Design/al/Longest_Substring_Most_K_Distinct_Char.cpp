class Solution {
public:
    int lengthOfLongestSubstringKDistinct(
        const std::string& s,
        int k) {

        if (s.empty() || k <= 0) {
            return 0;
        }

        std::vector<int> count(256, 0);

        int distinct = 0;
        int left = 0;
        int result = 0;

        for (int right = 0; right < static_cast<int>(s.size()); ++right) {
            unsigned char rightChar = static_cast<unsigned char>(s[right]);
            if (count[rightChar] == 0) {
                ++distinct;
            }

            ++count[rightChar];

            while (distinct > k) {
                unsigned char leftChar = static_cast<unsigned char>(s[left]);

                ++left;
                --count[leftChar];

                if (count[leftChar] == 0) {
                    --distinct;
                }
            }

            result = std::max(result, right - left + 1);
        }

        return result;
    }
};
