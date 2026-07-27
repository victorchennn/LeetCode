#include <string>
#include <vector>

class Solution {
public:
    std::string convert(std::string s, int numRows) {
        if (numRows == 1 || numRows >= s.size()) {
            return s;
        }

        std::vector<std::string> rows(numRows);

        int row = 0;
        bool goingDown = true;

        for (char c : s) {
            rows[row] += c;

            if (row == 0) {
                goingDown = true;
            } else if (row == numRows - 1) {
                goingDown = false;
            }

            row += goingDown ? 1 : -1;
        }

        std::string result;

        for (const auto& str : rows) {
            result += str;
        }

        return result;
    }
};
