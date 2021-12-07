package Companies.Microsoft;

/**
 * Given an integer n, return any array containing n unique integers such that they add up to 0.
 */
public class FindNUniqueIntegersSumuptoZero {
    public int[] sumZero(int n) {
        int[] A = new int[n];
        for (int i = 0; i < n; ++i)
            A[i] = i * 2 - n + 1;
        return A;
    }
}
