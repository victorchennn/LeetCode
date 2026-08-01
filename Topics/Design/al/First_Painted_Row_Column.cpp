// 如果 arr 里可能有重复数字怎么办？vector<bool> painted(rows * cols + 1, false);
// 如果 mat 里也有重复数字怎么办？ value -> pair<int, int> 不够 改成 unordered_map<int, vector<pair<int, int>>> positions;

class Solution {
public:
    int firstCompleteIndex(vector<int>& arr, vector<vector<int>>& mat) {
        int numRows = mat.size();
        int numCols = mat[0].size();
      
        // Map each value to its position (row, col) in the matrix
        unordered_map<int, pair<int, int>> valueToPosition;
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                valueToPosition[mat[row][col]] = {row, col};
            }
        }
      
        // Track how many elements are painted in each row and column
        vector<int> paintedInRow(numRows, 0);
        vector<int> paintedInCol(numCols, 0);
      
        // Process each element in arr sequentially
        for (int index = 0; ; ++index) {
            // Get the position of current element in the matrix
            auto [row, col] = valueToPosition[arr[index]];
          
            // Increment the count of painted elements for this row and column
            ++paintedInRow[row];
            ++paintedInCol[col];
          
            // Check if current row or column is completely painted
            if (paintedInRow[row] == numCols || paintedInCol[col] == numRows) {
                return index;
            }
        }
    }
};
