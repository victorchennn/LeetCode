package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.List;

/**
 * find all words in the board.
 */
public class WordSearchII {

public:
    vector<string> findWords(vector<vector<char>>& board,
                             const vector<string>& words) {
        vector<string> result;

        if (board.empty() || board[0].empty() || words.empty()) {
            return result;
        }

        unique_ptr<TrieNode> root = createTrie(words);

        for (int row = 0; row < static_cast<int>(board.size()); ++row) {
            for (int col = 0;
                 col < static_cast<int>(board[0].size());
                 ++col) {
                dfs(result, board, root.get(), row, col);
            }
        }

        return result;
    }

private:
    struct TrieNode {
        array<unique_ptr<TrieNode>, 26> children{};
        string word;
    };

    const int dirs_[4][2] = {
        {0, 1},
        {0, -1},
        {-1, 0},
        {1, 0}
    };

    unique_ptr<TrieNode> createTrie(const vector<string>& words) {
        auto root = make_unique<TrieNode>();

        for (const string& word : words) {
            TrieNode* current = root.get();

            for (char c : word) {
                int index = c - 'a';

                if (!current->children[index]) {
                    current->children[index] = make_unique<TrieNode>();
                }

                current = current->children[index].get();
            }

            current->word = word;
        }

        return root;
    }

    void dfs(vector<string>& result,
             vector<vector<char>>& board,
             TrieNode* node,
             int row,
             int col) {
        char c = board[row][col];

        if (c == '#') {
            return;
        }

        int index = c - 'a';

        if (!node->children[index]) {
            return;
        }

        TrieNode* child = node->children[index].get();

        if (!child->word.empty()) {
            result.push_back(child->word);

            // 防止同一个单词通过不同路径被重复加入结果。
            child->word.clear();
        }

        board[row][col] = '#';

        for (const auto& direction : dirs_) {
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];

            if (nextRow >= 0 &&
                nextRow < static_cast<int>(board.size()) &&
                nextCol >= 0 &&
                nextCol < static_cast<int>(board[0].size())) {
                dfs(result, board, child, nextRow, nextCol);
            }
        }

        board[row][col] = c;
    }

}
