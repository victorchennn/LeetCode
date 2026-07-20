package Companies.Bloomberg;

public class BestTimetoBuyandSellStock {
    /* Once */
    public int maxProfit(int[] prices) {
        if (prices.length == 0) {
            return 0;
        }
        int re = 0, min = prices[0];
        for (int i = 1; i < prices.length; i++) {
            min = Math.min(min, prices[i]);
            re = Math.max(re, prices[i]-min);
        }
        return re;
    }

    /* Multiple times */
    public int maxProfitII(int[] prices) {
        if (prices.length == 0) {
            return 0;
        }
        int sum = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i-1]) {
                sum += prices[i]-prices[i-1];
            }
        }
        return sum;
    }

    /* At most two transactions. */
    public int maxProfitIII(int[] prices) {
        int buy1 = Integer.MAX_VALUE, buy2 = Integer.MAX_VALUE, sell1 = 0, sell2 = 0;
        for (int price : prices) {                  // Assume we only have 0 money at first
            buy1 = Math.min(buy1, price);
            sell1 = Math.max(sell1, price-buy1);
            buy2  = Math.min(buy2, price-sell1);
            sell2 = Math.max(sell2, price-buy2);
        }
        return sell2;                               
    }

    /* At most K transactions. */
    public int maxProfitIV(int k, int[] prices) {
        if (k >= prices.length/2) {
            int re = 0;
            for (int i = 1; i < prices.length; i++) {
                if (prices[i] > prices[i-1])
                    re += prices[i] - prices[i-1];
            }
            return re;
        }
        int[][] dp = new int[k+1][prices.length];
        for (int i = 1; i <= k; i++) {
            int buy = prices[0];
            for (int j = 1; j < prices.length; j++) {
                buy = Math.min(buy, prices[j]-dp[i-1][j]);
                dp[i][j] = Math.max(dp[i][j-1], prices[j]-buy);
                
            }
        }
        return dp[k][prices.length-1];
    }

    /* Cooldown */
    public int maxProfitCooldown(int[] prices) {
        int buy = Integer.MAX_VALUE, prevbuy = 0, sell = 0, prevsell = 0;
        for (int price : prices) {
            prevbuy = buy;
            buy = Math.min(prevbuy, price-prevsell);
            prevsell = sell;
            sell = Math.max(prevsell, price-prevbuy);
        }
        return sell;
    }

    /* Transaction Fee */
    public int maxProfit(int[] prices, int fee) {
        int buy = -prices[0], sell = 0;
        for (int i = 1; i < prices.length; i++) {
            buy = Math.max(buy, sell-prices[i]);      // dont trade or use previous profit to buy
            sell = Math.max(sell, buy+prices[i]-fee); // dont trade keep profit or sell today 
        }
        return sell;
    }
}
