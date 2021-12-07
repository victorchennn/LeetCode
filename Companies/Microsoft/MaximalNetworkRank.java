package Companies.Microsoft;

/**
 * The network rank of two different cities is defined as the total number of directly connected
 * roads to either city. If a road is directly connected to both cities, it is only counted once.
 *
 * The maximal network rank of the infrastructure is the maximum network rank of all pairs of different cities.
 */
public class MaximalNetworkRank {
    public int maximalNetworkRank(int n, int[][] roads) {
        if (roads.length == 0 || roads[0].length == 0) {
            return 0;
        }
        boolean[][] connected = new boolean[n][n];
        int[] count = new int[n];
        for (int[] road : roads) {
            count[road[0]]++;
            count[road[1]]++;
            connected[road[0]][road[1]] = true;
            connected[road[1]][road[0]] = true;
        }
        int re = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                re = Math.max(re, count[i]+count[j]-(connected[i][j]?1:0));
            }
        }
        return re;
    }
}
