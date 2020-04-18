package Companies.Twitter;

public class GetSetGo {
    private static boolean isSubsetSum(int set[], int sum) {
        int n = set.length;
        boolean subset[][] = new boolean[sum+1][n+1];
        for (int i = 0; i <= n; i++)
            subset[0][i] = true;
        for (int i = 1; i <= sum; i++) {
            for (int j = 1; j <= n; j++) {
                subset[i][j] = subset[i][j-1];
                if (i >= set[j-1])
                    subset[i][j] = subset[i][j] || subset[i - set[j-1]][j-1];
            }
        }
        return subset[sum][n];
    }

    public static void main(String...args) {
        System.out.println(isSubsetSum(new int[]{3,34,4,12,5,2}, 9));
        System.out.println(isSubsetSum(new int[]{2,9,5,1,6}, 12));
        System.out.println(isSubsetSum(new int[]{2,3,15,1,16}, 8));
    }
}
