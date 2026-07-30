class Solution {
public:
    int countSubstrings(string s) {
        int result = 0;
        // 如果要求最长回文子串？int bestLeft = 0; bestLength = 0;

        for (int i = 0; i < static_cast<int>(s.size()); ++i) {
            result += expand(s, i, i);       // 奇数长度
            result += expand(s, i, i + 1);   // 偶数长度
        }

        return result;
    }

    int expand(const string& s, int left, int right) {
        int count = 0;

        while (left >= 0 &&
               right < static_cast<int>(s.size()) &&
               s[left] == s[right]) {
            ++count;
            // int length = right - left + 1;
            // if (length > bestLength) {
            //     bestLength = length;
            //     bestLeft = left;
            // }
            --left;
            ++right;
        }

        return count;
    }

    int countSubstrings(string s) {
        int n = s.size();
        int result = 0;
        vector<vector<bool>> dp(n, vector<bool>(n, false));
    
        for (int r = 0; r < n; ++r) {
            for (int l = r; l >= 0; --l) {
                if (s[l] == s[r] &&
                    (r - l <= 2 || dp[l + 1][r - 1])) {
                    dp[l][r] = true;
                    ++result;
                }
            }
        }
    
        return result;
    }
};
