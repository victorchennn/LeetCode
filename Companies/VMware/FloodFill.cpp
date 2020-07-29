#include <iostream>
#include <vector>

using namespace std;

class FloodFill
{
public:
  vector<vector<int>> floodFill(vector<vector<int>> &image, int sr, int sc, int newColor)
  {
    int color = image[sr][sc];
    if (color != newColor)
    {
      dfs(image, sr, sc, color, newColor);
    }
    return image;
  }

private:
  void dfs(vector<vector<int>> &image, int r, int c, int color, int newColor)
  {
    if (image[r][c] == color)
    {
      image[r][c] = newColor;
      if (r >= 1)
        dfs(image, r - 1, c, color, newColor);
      if (c >= 1)
        dfs(image, r, c - 1, color, newColor);
      if (r < image.size() - 1)
        dfs(image, r + 1, c, color, newColor);
      if (c < image[0].size() - 1)
        dfs(image, r, c + 1, color, newColor);
    }
  }
};