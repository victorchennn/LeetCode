class Solution {
private:
    const vector<pair<int, int>> dirs{{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

public:
    int numIslands(vector<vector<char>>& grid) {
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }

        int rows = grid.size();
        int cols = grid[0].size();
        int result = 0;

        vector<vector<bool>> visited(rows, vector<bool>(cols, false));
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                if (grid[row][col] == '1' && !visited[row][col]) {
                    ++result;
                    dfs(grid, row, col, visited);
                }
            }
        }
        return result;
    }

private:
    void dfs(const vector<vector<char>>& grid, int row, int col, vector<vector<bool>>& visited) {
        visited[row][col] = true;

        for (const auto& [dx, dy] : dirs) {
            int nextRow = row + dx;
            int nextCol = col + dy;

            if (nextRow < 0 || nextRow >= grid.size() ||
                nextCol < 0 || nextCol >= grid[0].size() ||
                grid[nextRow][nextCol] == '0' ||
                visited[nextRow][nextCol]) {
                continue;
            }

            dfs(grid, nextRow, nextCol, visited);
        }
    }
};

public:
    int numDistinctIslands(vector<vector<int>>& grid) {
        set<string> shapes;

        for (int row = 0; row < grid.size(); ++row) {
            for (int col = 0; col < grid[0].size(); ++col) {
                if (grid[row][col] == 1) {
                    string shape;
                    dfs(grid, row, col, 0, 0, shape);
                    shapes.insert(shape);
                }
            }
        }

        return shapes.size();
    }

private:
    void dfs(vector<vector<int>>& grid, int row, int col, int relativeRow, int relativeCol, string& shape) {
        if (row < 0 || row >= grid.size() || col < 0 || col >= grid[0].size() || grid[row][col] == 0) {
            return;
        }

        grid[row][col] = 0;

        shape += to_string(relativeRow);
        shape += ",";
        shape += to_string(relativeCol);
        shape += ";";

        for (const auto& [dr, dc] : dirs) {
            dfs(grid, row + dr, col + dc, relativeRow + dr, relativeCol + dc, shape);
        }
    }
};
