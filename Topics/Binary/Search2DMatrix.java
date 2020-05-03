package Topics.Binary;

public class Search2DMatrix {
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        int m = matrix.length, n = matrix[0].length;
        int l = 0, r = m*n-1;
        while (l <= r) {
            int mid = l + (r-l)/2;
            if (matrix[mid/n][mid%n] == target) {
                return true;
            }
            if (matrix[mid/n][mid%n] > target) {
                r = mid-1;
            } else {
                l = mid+1;
            }
        }
        return false;
    }

    /* Integers in each row are sorted in ascending from left to right. */
    /* Integers in each column are sorted in ascending from top to bottom. */
    public boolean searchMatrixII(int[][] matrix, int target) {
        if (matrix.length == 0) {
            return false;
        }
        int r = 0, c = matrix[0].length-1;
        while (r < matrix.length && c >= 0) {
            if (matrix[r][c] == target) {
                return true;
            }
            if (matrix[r][c] > target) {
                c--;
            } else {
                r++;
            }
        }
        return false;
    }
}
