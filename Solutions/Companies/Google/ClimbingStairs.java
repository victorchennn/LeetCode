package Companies.Google;

public class ClimbingStairs {
    /**
     * @see Companies.Bloomberg.FibonacciNumber
     */
    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        int num1 = 1, num2 = 2;
        for (int i = 3; i <= n; i++) {
            int temp = num1 + num2;
            num1 = num2;
            num2 = temp;
        }
        return num2;
    }

    /**
     * Input: cost = [1, 100, 1, 1, 1, 100, 1, 1, 100, 1]
     * Output: 6
     *
     * Once pay the cost, can either climb one or two steps.
     * Find minimum cost to reach the top of the floor, and
     * can either start from the step with index 0, or the step with index 1.
     */
    public int minCostClimbingStairs(int[] cost) {
        int twoStep = cost[0];
        int oneStep = cost[1]; // if start from index0, oneStep += cost[0]
        int curr = 0;
        for(int i = 2; i < cost.length; i++){
            curr = Math.min(twoStep, oneStep) + cost[i];
            twoStep = oneStep;
            oneStep = curr;
        }
        return Math.min(oneStep,twoStep);
    }

    /* Modify the arary */
    public int minCostClimbingStairsII(int[] cost) {
        for (int i = 2; i < cost.length; i++) {
            cost[i] += Math.min(cost[i-1], cost[i-2]);
        }
        return Math.min(cost[cost.length-1], cost[cost.length-2]);
    }
}
