package Companies.VMware;

import java.util.Arrays;

/**
 * Given an array A of non-negative integers, return an array consisting of
 * all the even elements of A, followed by all the odd elements of A.
 */
public class SortArrayByParity {
    public int[] sortArrayByParity(int[] A) {
        int i = 0, j = A.length-1;
        while (i < j) {
            if (A[i]%2 > A[j]%2) {
                int temp = A[i];
                A[i] = A[j];
                A[j] = temp;
            }
            if ((A[i]&1) == 0) {
                i++;
            }
            if ((A[j]&1) == 1) {
                j--;
            }
        }
        return A;

//        return Arrays.stream(A)
//                .boxed()
//                .sorted((a, b)->Integer.compare(a%2, b%2))
//                .mapToInt(i -> i)
//                .toArray();
    }
}
