#include <array>
#include <queue>
#include <utility>
#include <vector>

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
