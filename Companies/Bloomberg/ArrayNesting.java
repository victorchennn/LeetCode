package Companies.Bloomberg;

/**
 * The array of size N contains number [0, N-1]
 * Find the longest length of set S, where S[i] = {A[i], A[A[i]], A[A[A[i]]], ... }
 *
 * We can know that in-degree of all nodes are exactly one (n different edges for n nodes).
 * Therefore the graph should consist of several cycles and the cycles have no "tails".
 * That's why we can skip the visited nodes, where to begin in a visited cycle doesn't matter in this circumstance.
 */
public class ArrayNesting {
    public int arrayNesting(int[] nums) {
        int[] copy = nums;
        int maxsize = 0;
        for (int i = 0; i < copy.length; i++) {
            int size = 0;
            for (int k = i; copy[k] >= 0; size++) {
                int ak = copy[k];
                copy[k] = -1; // mark copy[k] as visited;
                k = ak;
            }
            maxsize = Integer.max(maxsize, size);
        }
        return maxsize;
    }
}
