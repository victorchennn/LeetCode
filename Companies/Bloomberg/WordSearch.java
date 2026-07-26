package Companies.Bloomberg;

/**
 * Time Complexity: O(N*4^L), N is the number of cells and L is the length of the word
 * Space Complexity: O(L)
 */
#include <string>
#include <vector>

using namespace std;

class WordSearch {
private:
    const vector<pair<int, int>> dirs{
        {0, 1},
        {0, -1},
        {-1, 0},
        {1, 0}
    };

    bool dfs(vector<vector<char>>& board, const string& word, int row, int col, int index) {
        if (index == static_cast<int>(word.size())) {
            return true;
        }

        if (row < 0 || row >= static_cast<int>(board.size()) ||
            col < 0 || col >= static_cast<int>(board[0].size()) ||
            board[row][col] != word[index]) {
            return false;
        }

        char original = board[row][col];
        board[row][col] = '#';

        for (const auto& [dr, dc] : dirs) {
            if (dfs(board, word, row + dr, col + dc, index + 1)) {
                board[row][col] = original;
                return true;
            }
        }

        board[row][col] = original;
        return false;
    }

public:
    bool exist(vector<vector<char>>& board, const string& word) {
        if (word.empty()) {
            return true;
        }

        if (board.empty() || board[0].empty()) {
            return false;
        }

        for (int row = 0; row < static_cast<int>(board.size()); ++row) {
            for (int col = 0;
                 col < static_cast<int>(board[0].size());
                 ++col) {
                if (dfs(board, word, row, col, 0)) {
                    return true;
                }
            }
        }

        return false;
    }
};
