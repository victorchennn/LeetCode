#include <iostream>
#include <vector>

using namespace std;

class MaximalSquare
{
public:
  int maximalSquare(vector<vector<char>> &matrix)
  {
    if (matrix.empty())
    {
      return 0;
    }
    int m = matrix.size(), n = matrix[0].size(), re = 0, prev = 0;
    vector<int> dp(n + 1, 0);
    for (int i = 1; i <= m; i++)
    {
      for (int j = 1; j <= n; j++)
      {
        int temp = dp[j];
        if (matrix[i - 1][j - 1] == '1')
        {
          dp[j] = 1 + min(dp[j], min(dp[j - 1], prev));
          re = max(re, dp[j]);
        }
        else
        {
          dp[j] = 0;
        }
        prev = temp;
      }
    }
    return re * re;
  }
};