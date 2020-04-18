package Companies.Google;

public class MaxChunksMakeSortedII {
    public int maxChunksToSorted(int[] arr) {
        int n = arr.length;
        int[] leftMax = new int[n];
        int[] rightMin = new int[n];

        leftMax[0] = arr[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i-1], arr[i]);
        }

        rightMin[n-1] = arr[n-1];
        for (int i = n-2; i >= 0; i--) {
            rightMin[i] = Math.min(rightMin[i+1], arr[i]);
        }

        int chunks = 1;
        for (int i = 0; i < n-1; i++) {
            if (leftMax[i] <= rightMin[i+1]) {
                chunks++;
            }
        }

        return chunks;

    }
}
