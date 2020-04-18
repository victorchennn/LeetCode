package Companies.Google;

public class MaxChunksMakeSorted {
    public int maxChunksToSorted(int[] arr) {
        int max = 0, chunk = 0;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
            if (max == i) {
                chunk++;
            }
        }
        return chunk;
    }
}
