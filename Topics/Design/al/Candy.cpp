// Input: ratings = [1,0,2]
// Output: 5
// Explanation: You can allocate to the first, second and third child with 2, 1, 2 candies respectively.
class Solution {
public:
    int candy(vector<int>& ratings) {
        int n = ratings.size();
        vector<int> candies(n, 1);

        // 从左往右：保证比左边 rating 高的人，糖更多
        for (int i = 1; i < n; ++i) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }

        // 从右往左：保证比右边 rating 高的人，糖更多
        for (int i = n - 2; i >= 0; --i) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = max(candies[i], candies[i + 1] + 1);
            }
        }

        return accumulate(candies.begin(), candies.end(), 0);
    }
};

// 0(1) space
class Solution {
public:
    int candy(vector<int>& ratings) {
        int n = ratings.size();
        if (n <= 1) return n;

        int total = 1;
        int up = 0;
        int down = 0;
        int peak = 0;

        for (int i = 1; i < n; ++i) {
            if (ratings[i] > ratings[i - 1]) { // 上坡
                ++up;
                peak = up;
                down = 0;
                total += 1 + up;
            }
            else if (ratings[i] == ratings[i - 1]) { // 相等，没有额外约束
                up = down = peak = 0;
                total += 1;
            }
            else { // 下坡
                up = 0;
                ++down;

                // 下坡上所有人都要被重新抬高
                total += 1 + down;
                if (down <= peak) { // peak 已经够高，不需要把 peak 再抬一次
                    --total;
                } // else:
                  // 下坡比原来的上坡还长，
                  // peak 也必须跟着抬高，所以不减
            }
        }

        return total;
    }
};
