package Topics.Binary;

/**
 * Given an input array nums, where nums[i] ≠ nums[i+1], find a peak element's index.
 * A peak element is an element that is greater than its neighbors.
 *
 * The array may contain multiple peaks, in that case return the index to any one of the peaks.
 *
 * @see PeakIndexinaMountainArray
 */
public class FindPeakElement {
    public int findPeakElement(int[] nums) {
        int l = 0, r = nums.length-1;
        while (l < r) {
            int mid = (l+r)/2;
            if (nums[mid] > nums[mid+1]) {
                r = mid;
            } else {
                l = mid+1;
            }
        }
        return l;
    }
}

// A peak element in a 2D grid is an element that is strictly greater than all of its adjacent neighbors to the left, right, top, and bottom.

// Given a 0-indexed m x n matrix mat where no two adjacent cells are equal, find any peak element mat[i][j] and return the length 2 array [i,j].

// You may assume that the entire matrix is surrounded by an outer perimeter with the value -1 in each cell.

// You must write an algorithm that runs in O(m log(n)) or O(n log(m)) time.
    public int[] findPeakGrid(int[][] mat) {
        int leftRow = 0;
        int rightRow = mat.length - 1;
        int numColumns = mat[0].length;

        // Binary search on rows
        while (leftRow < rightRow) {
            int midRow = (leftRow + rightRow) >> 1;  // Equivalent to (leftRow + rightRow) / 2

            // Find the column index of the maximum element in the middle row
            int maxColumnIndex = maxPos(mat[midRow]);

            // Compare the maximum element with the element directly below it
            // If current max is greater, the peak must be in the upper half (including midRow)
            if (mat[midRow][maxColumnIndex] > mat[midRow + 1][maxColumnIndex]) {
                rightRow = midRow;
            } else {
                // Otherwise, the peak must be in the lower half (excluding midRow)
                leftRow = midRow + 1;
            }
        }

        // Return the peak position: leftRow and the column with max value in that row
        return new int[] {leftRow, maxPos(mat[leftRow])};}
    
    /**
     * Finds the index of the maximum element in a 1D array.
     *
     * @param arr The input array
     * @return The index of the maximum element
     */
    private int maxPos(int[] arr) {
        int maxIndex = 0;

        // Iterate through the array to find the maximum element's index
        for (int i = 1; i < arr.length; ++i) {
            if (arr[maxIndex] < arr[i]) {
                maxIndex = i;
            }
        }

        return maxIndex;
    
    }
