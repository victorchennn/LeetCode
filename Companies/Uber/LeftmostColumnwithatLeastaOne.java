package Companies.Uber;

import java.util.List;

/**
 * Given a row-sorted binary matrix binaryMatrix, return the index (0-indexed) of the
 * leftmost column with a 1 in it. If such an index does not exist, return -1.
 *
 *
 * // This is the BinaryMatrix's API interface.
 * // You should not implement it, or speculate about its implementation
 *   interface BinaryMatrix {
 *      public int get(int row, int col) {}
 *      public List<Integer> dimensions {}
 *   };
 */
public class LeftmostColumnwithatLeastaOne {
//    /* Linear Search */
//    public int leftMostColumnWithOne(BinaryMatrix binaryMatrix) {
//        List<Integer> dimen = binaryMatrix.dimensions();
//        int rows = dimen.get(0), cols = dimen.get(1);
//        int re = -1, r = 0, c = cols-1;
//        while (r < rows && c >= 0) {
//            if (binaryMatrix.get(r, c) == 1) {
//                re = c;
//                c--;
//            } else {
//                r++;
//            }
//        }
//        return re;
//    }
//
//    /* Binary Search */
//    public int leftMostColumnWithOne(BinaryMatrix binaryMatrix) {
//        List<Integer> dimen = binaryMatrix.dimensions();
//        int m = dimen.get(0), n = dimen.get(1);
//        int left = 0, right = n - 1, ans = -1;
//        while (left <= right) {
//            int mid = left + (right - left) / 2;
//            if (existOneInColumn(binaryMatrix, m, mid)) {
//                ans = mid;          // record as current ans
//                right = mid - 1;    // try to find in the left side
//            } else {
//                left = mid + 1;     // try to find in the right side
//            }
//        }
//        return ans;
//    }
//
//    boolean existOneInColumn(BinaryMatrix binaryMatrix, int m, int c) {
//        for (int r = 0; r < m; r++) if (binaryMatrix.get(r, c) == 1) return true;
//        return false;
//    }
}


