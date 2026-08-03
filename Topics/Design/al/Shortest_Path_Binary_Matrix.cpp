// Given an n x n binary matrix grid, return the length of the shortest clear path in the matrix. If there is no clear path, return -1.
// A clear path in a binary matrix is a path from the top-left cell (i.e., (0, 0)) to the bottom-right cell (i.e., (n - 1, n - 1)) such that:

// All the visited cells of the path are 0.
// All the adjacent cells of the path are 8-directionally connected (i.e., they are different and they share an edge or a corner).

// The length of a clear path is the number of visited cells of this path.
// Input: grid = [[0,0,0],[1,1,0],[1,1,0]]
// Output: 4

class Solution {
public:
    int shortestPathBinaryMatrix(std::vector<std::vector<int>>& grid) {
        int rows = static_cast<int>(grid.size());
        int cols = static_cast<int>(grid[0].size());

        if (grid[0][0] == 1 || grid[rows - 1][cols - 1] == 1) {
            return -1;
        }

        static constexpr std::array<std::pair<int, int>, 8> directions{{
            {0, 1},
            {0, -1},
            {1, 0},
            {-1, 0},
            {1, 1},
            {1, -1},
            {-1, 1},
            {-1, -1}
        }};

        std::queue<std::pair<int, int>> queue;
        queue.push({0, 0});

        // 直接修改 grid，避免额外 visited 数组。
        grid[0][0] = 1;

        int distance = 1;

        while (!queue.empty()) {
            int levelSize = static_cast<int>(queue.size());

            for (int i = 0; i < levelSize; ++i) {
                auto [row, col] = queue.front();
                queue.pop();

                if (row == rows - 1 && col == cols - 1) {
                    return distance;
                }

                for (const auto& [dr, dc] : directions) {
                    int nextRow = row + dr;
                    int nextCol = col + dc;

                    if (nextRow < 0 || nextRow >= rows ||
                        nextCol < 0 || nextCol >= cols ||
                        grid[nextRow][nextCol] != 0) {
                        continue;
                    }

                    // 加入队列时立刻标记，而不是取出时再标记。
                    grid[nextRow][nextCol] = 1;
                    queue.push({nextRow, nextCol});
                }
            }

            ++distance;
        }

        return -1;
    }
};
