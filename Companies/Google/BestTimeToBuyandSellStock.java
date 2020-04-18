package Companies.Google;

public class BestTimeToBuyandSellStock {
    public int maxProfit(int[] prices) {
        if (prices.length == 0) {
            return 0;
        }
        int max = prices[prices.length-1];
        int re = 0;
        for (int i = prices.length-2; i >= 0; i--) {
            max = Math.max(max, prices[i]);
            re = Math.max(re, max - prices[i]);
        }
        return re;
    }

    public int maxProfit(int[] prices, int fee) {
        int cash = 0, hold = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            cash = Math.max(cash, hold+prices[i]-fee);
            hold = Math.max(hold, cash-prices[i]);
        }
        return cash;
    }

    /**
     * [1,5,3,1,5]
     * [1,5,1,5]
     */
    public int maxProfit_cooldown(int[] prices) {
        int sell = 0, prevsell = 0, buy = Integer.MIN_VALUE, prevbuy = 0;
        for (int p : prices) {
            prevbuy = buy;
            buy = Math.max(buy, prevsell-p);
            prevsell = sell;
            sell = Math.max(sell, prevbuy+p);
        }
        return sell;
    }

    public static void main(String...args) {
        BestTimeToBuyandSellStock test = new BestTimeToBuyandSellStock();
        System.out.println(test.maxProfit(new int[]{1,5,1,4}, 2));

        System.out.println(test.maxProfit_cooldown(new int[]{1,5,1,4}));
        System.out.println(test.maxProfit_cooldown(new int[]{1,5,1,4,6}));
        System.out.println(test.maxProfit_cooldown(new int[]{1,5,3,1,5}));
    }
}
