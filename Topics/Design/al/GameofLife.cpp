class Solution {
public:
    void gameOfLife(vector<vector<int>>& board) {
        static constexpr array<pair<int, int>, 8> directions{{
            {1, 1}, {1, 0}, {1, -1}, {0, -1},
            {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}
        }};

        int rows = static_cast<int>(board.size());
        int cols = static_cast<int>(board[0].size());

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                int liveNeighbors = 0;

                for (const auto& [dr, dc] : directions) {
                    int nextRow = row + dr;
                    int nextCol = col + dc;

                    if (nextRow >= 0 && nextRow < rows &&
                        nextCol >= 0 && nextCol < cols &&
                        abs(board[nextRow][nextCol]) == 1) {
                        ++liveNeighbors;
                    }
                }

                // 1 -> -1: 原来活着，下一轮死亡
                if (board[row][col] == 1 &&
                    (liveNeighbors < 2 || liveNeighbors > 3)) {
                    board[row][col] = -1;
                }

                // 0 -> 2: 原来死亡，下一轮复活
                if (board[row][col] == 0 && liveNeighbors == 3) {
                    board[row][col] = 2;
                }
            }
        }

        for (auto& row : board) {
            for (int& cell : row) {
                cell = cell > 0 ? 1 : 0;
            }
        }
    }
};

// 不要扫描所有格子，只处理活细胞及其邻居
// 假设只有少量活细胞，我们用一个哈希集合存活细胞坐标：
// 每一轮只遍历当前活细胞。对于每个活细胞，把它周围 8 个位置的邻居数量加一：
// 最后只检查 neighborCount 中出现过的格子。因为一个完全远离活细胞的死格子，邻居数一定是 0，不可能突然复活，所以根本不用处理。
class GameOfLife {
private:
    struct PairHash {
        size_t operator()(const pair<int, int>& cell) const noexcept {
            size_t h1 = hash<int>{}(cell.first);
            size_t h2 = hash<int>{}(cell.second);
            return h1 ^ (h2 << 1);
        }
    };

    using Cell = pair<int, int>;

public:
    unordered_set<Cell, PairHash> nextGeneration(const unordered_set<Cell, PairHash>& liveCells) {
        unordered_map<Cell, int, PairHash> neighborCount;

        for (const auto& [row, col] : liveCells) {
            for (const auto& [dr, dc] : directions) {
                ++neighborCount[{row + dr, col + dc}];
            }
        }

        unordered_set<Cell, PairHash> nextLiveCells;

        for (const auto& [cell, count] : neighborCount) {
            bool currentlyAlive = liveCells.contains(cell);

            if (count == 3 || (currentlyAlive && count == 2)) {
                nextLiveCells.insert(cell);
            }
        }

        return nextLiveCells;
    }
};
