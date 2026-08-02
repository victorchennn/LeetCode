输入是一个正整数n 只有三个操作, 求通过下面三个操作多少步数可以到1
n -> n - 1 -> n / 2 (n % 2 == 0) -> n / 3 (n % 3 == 0) 
例子： n -> 1 
n = 4 4 -> 3 -> 2 -> 1 3 steps 
      4 -> 3 -> 1 2 steps
      4 -> 2 -> 1 2 steps
  
class Solution {
public:
    int minSteps(int n) {
        if (n <= 1) {
            return 0;
        }

        vector<int> dp(n + 1, 0);
        for (int i = 2; i <= n; ++i) {
            dp[i] = dp[i - 1] + 1;
            if (i % 2 == 0) {
                dp[i] = min(dp[i], dp[i / 2] + 1);
            }
            if (i % 3 == 0) {
                dp[i] = min(dp[i], dp[i / 3] + 1);
            }
        }
        return dp[n];
    }
};

// 如何返回操作路径？
class Solution {
public:
    vector<int> minPath(int n) {
        if (n <= 0) {
            return {};
        }

        vector<int> dp(n + 1, 0);
        vector<int> parent(n + 1, -1);

        for (int i = 2; i <= n; ++i) {
            dp[i] = dp[i - 1] + 1;
            parent[i] = i - 1;

            if (i % 2 == 0 && dp[i / 2] + 1 < dp[i]) {
                dp[i] = dp[i / 2] + 1;
                parent[i] = i / 2;
            }
          
            if (i % 3 == 0 && dp[i / 3] + 1 < dp[i]) {
                dp[i] = dp[i / 3] + 1;
                parent[i] = i / 3;
            }
        }

        vector<int> path;
        for (int current = n; current != -1; current = parent[current]) {
            path.push_back(current);
            if (current == 1) {
                break;
            }
        }
        return path;
    }
};
