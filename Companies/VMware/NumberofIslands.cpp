#include <iostream>
#include <vector>
#include <queue>

using namespace std;

class NumberofIslands
{
public:
  int numIslands(vector<vector<char>> &grid) // bfs
  {
    int m = grid.size(), n = m ? grid[0].size() : 0, offsets[] = {0, 1, 0, -1, 0};
    int re = 0;
    for (int i = 0; i < m; i++)
    {
      for (int j = 0; j < n; j++)
      {
        if (grid[i][j] == '1')
        {
          re++;
          grid[i][j] = '0';
          queue<pair<int, int>> neighbors;
          neighbors.push({i, j});
          while (!neighbors.empty())
          {
            pair<int, int> neigh = neighbors.front();
            neighbors.pop();
            for (int k = 0; k < 4; k++)
            {
              int r = neigh.first + offsets[k];
              int c = neigh.second + offsets[k + 1];
              if (r >= 0 && r < m && c >= 0 && c < n && grid[r][c] == '1')
              {
                grid[r][c] = '0';
                neighbors.push({r, c});
              }
            }
          }
        }
      }
    }
    return re;
  }

  int numIslandsII(vector<vector<char>> &grid) // dfs
  {
    int m = grid.size(), n = m ? grid[0].size() : 0;
    int re = 0;
    for (int i = 0; i < m; i++)
    {
      for (int j = 0; j < n; j++)
      {
        if (grid[i][j] == '1')
        {
          re++;
          dfs(grid, i, j);
        }
      }
    }
    return re;
  }

private:
  void dfs(vector<vector<char>> &grid, int i, int j)
  {
    int m = grid.size(), n = grid[0].size();
    if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] == '0')
    {
      return;
    }
    grid[i][j] = '0';
    dfs(grid, i + 1, j);
    dfs(grid, i - 1, j);
    dfs(grid, i, j + 1);
    dfs(grid, i, j - 1);
  }
};