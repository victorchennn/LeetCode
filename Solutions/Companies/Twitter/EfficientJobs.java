package Companies.Twitter;

public class EfficientJobs {
    public int solution(int[] weights, int[] tasks, int p) {
        int[][] dp = new int[tasks.length + 1][p/2 + 1];
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[0].length; j++) {
                if (j < tasks[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - tasks[i - 1]] + weights[i - 1]);
                }
            }
        }
        return dp[tasks.length][p / 2];
    }

    public static void main(String...args) {
        EfficientJobs test = new EfficientJobs();
        System.out.println(test.solution(new int[]{2,4,4,5}, new int[]{2,2,3,4}, 15));
        System.out.println(test.solution(new int[]{3,2,2}, new int[]{3,2,2}, 9));
    }
}
