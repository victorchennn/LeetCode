package Companies.Amazon;

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
        int buy1 = Integer.MIN_VALUE, buy2 = Integer.MIN_VALUE, sell1 = 0, sell2 = 0;
        for (int price : prices) {                  // Assume we only have 0 money at first
            sell2 = Math.max(sell2, buy2+price);    // The maximum if we've just sold 2nd stock so far.
            buy2 = Math.max(buy2, sell1-price);     // The maximum if we've just buy  2nd stock so far.
            sell1 = Math.max(sell1, buy1+price);    // The maximum if we've just sold 1nd stock so far.
            buy1 = Math.max(buy1, -price);          // The maximum if we've just buy  1st stock so far.
        }
        return sell2;                               // Since release1 is initiated as 0, so release2 will always higher than release1.
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
            int buy = -prices[0];
            for (int j = 1; j < prices.length; j++) {
                dp[i][j] = Math.max(dp[i][j-1], prices[j]+buy);
                buy = Math.max(buy, dp[i-1][j]-prices[j]);
            }
        }
        return dp[k][prices.length-1];
    }

    /* Cooldown */
    public int maxProfitCooldown(int[] prices) {
        int buy = Integer.MIN_VALUE, prevbuy = 0, sell = 0, prevsell = 0;
        for (int price : prices) {
            prevbuy = buy;
            buy = Math.max(prevbuy, prevsell-price);
            prevsell = sell;
            sell = Math.max(prevsell, prevbuy+price);
        }
        return sell;
    }

    /* Transaction Fee */
    public int maxProfit(int[] prices, int fee) {
        int buy = -prices[0], sell = 0;
        for (int i = 1; i < prices.length; i++) {
            sell = Math.max(sell, buy+prices[i]-fee); // sell first, since sell and buy at same day can't be
            buy = Math.max(buy, sell-prices[i]);      // better than just continuing to hold the stock
        }
        return sell;
    }
}
