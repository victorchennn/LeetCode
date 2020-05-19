package Companies.Bloomberg;

public class FillingBookcaseShelves {
    public int minHeightShelves(int[][] books, int shelf_width) {
        int[] dp = new int[books.length+1];
        for (int i = 0; i < books.length; i++) {
            int width = books[i][0], height = books[i][1];
            dp[i+1] = dp[i] + height;
            for (int j = i-1; j >= 0 && width + books[j][0] <= shelf_width; j--) {
                width += books[j][0];
                height = Math.max(height, books[j][1]);
                dp[i+1] = Math.min(dp[i+1], dp[j]+height);
            }
        }
        return dp[books.length];
    }
}
