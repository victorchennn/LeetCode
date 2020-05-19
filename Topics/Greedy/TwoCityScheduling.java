package Topics.Greedy;

import java.util.Arrays;

public class TwoCityScheduling {
    public int twoCitySchedCost(int[][] costs) {
        // Sort by a gain which company has by sending a person to city A and not to city B
        Arrays.sort(costs, (o1, o2)->o1[0]-o1[1]-(o2[0]-o2[1]));
        int cost = 0, n = costs.length/2;

        // To optimize the company expenses,
        // send the first n persons to the city A and the others to the city B
        for (int i = 0; i < n; i++) {
            cost += costs[i][0] + costs[i+n][1];
        }
        return cost;
    }
}
