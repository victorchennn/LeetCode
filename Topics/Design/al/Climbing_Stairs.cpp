#include <algorithm>
#include <vector>
using namespace std;

class ClimbingStairs {
public:
    // LeetCode 70: Climbing Stairs
    int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }

        int twoStep = 1;  // dp[i - 2]
        int oneStep = 2;  // dp[i - 1]

        for (int i = 3; i <= n; ++i) {
            int current = twoStep + oneStep;
            twoStep = oneStep;
            oneStep = current;
        }

        return oneStep;
    }

    // LeetCode 746: Min Cost Climbing Stairs
    // 不修改原数组，空间 O(1)
    int minCostClimbingStairs(const vector<int>& cost) {
        int twoStep = cost[0];  // 到达 i - 2 的最小花费
        int oneStep = cost[1];  // 到达 i - 1 的最小花费

        for (int i = 2; i < cost.size(); ++i) {
            int current = min(twoStep, oneStep) + cost[i];
            twoStep = oneStep;
            oneStep = current;
        }

        // 顶层位于数组末尾的下一层，可以从最后一级或倒数第二级到达
        return min(twoStep, oneStep);
    }

    // 修改原数组，空间 O(1)
    int minCostClimbingStairsII(vector<int>& cost) {
        for (int i = 2; i < cost.size(); ++i) {
            cost[i] += min(cost[i - 1], cost[i - 2]);
        }

        return min(cost[cost.size() - 1], cost[cost.size() - 2]);
    }
};
