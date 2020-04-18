package Companies.Microsoft;

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

    /* Cooldown */
    public int maxProfitCooldown(int[] prices) {
        int buy = Integer.MIN_VALUE, prevbuy = 0, sell = 0, prevsell = 0;
        for (int p : prices) {
            prevbuy = buy;
            buy = Math.max(buy, prevsell-p);
            prevsell = sell;
            sell = Math.max(sell, prevbuy+p);
        }
        return sell;
    }

    /* Transaction Fee */
    public int maxProfit(int[] prices, int fee) {
        int buy = -prices[0], sell = 0;
        for (int i = 1; i < prices.length; i++) {
            sell = Math.max(sell, buy+prices[i]-fee);
            buy = Math.max(buy, sell-prices[i]);
        }
        return sell;
    }
}
