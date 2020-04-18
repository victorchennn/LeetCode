package Companies.Bloomberg;

import java.util.List;

public class Triangle {
    public int minimumTotal(List<List<Integer>> triangle) {
        int[] dp = new int[triangle.size()+1];
        for (int i = triangle.size()-1; i >= 0; i--) {
            for (int j = 0; j < triangle.get(i).size(); j++) {
                dp[j] = triangle.get(i).get(j) + Math.min(dp[j], dp[j+1]);
            }
        }
        return dp[0];

        // if (triangle.size() == 0) {
        //     return 0;
        // }
        // for (int i = triangle.size()-2; i >= 0; i--) {
        //     List<Integer> next = triangle.get(i+1);
        //     for (int j = 0; j < next.size()-1; j++) {
        //         int num = triangle.get(i).get(j) + Math.min(next.get(j), next.get(j+1));
        //         triangle.get(i).set(j, num);
        //     }
        // }
        // return triangle.get(0).get(0);
    }
}
