public class BestTimetoBuyandSellStock {
    /* Once */
    int maxProfit(const std::vector<int>& prices) {
        if (prices.empty()) {
            return 0;
        }

        int minPrice = prices[0];
        int result = 0;

        for (int i = 1; i < static_cast<int>(prices.size()); ++i) {
            minPrice = std::min(minPrice, prices[i]);
            result = std::max(result, prices[i] - minPrice);
        }
        
        return result;
    }

    /* Multiple times */
    int maxProfitII(const std::vector<int>& prices) {
        int totalProfit = 0;

        for (int i = 1; i < static_cast<int>(prices.size()); ++i) {
            if (prices[i] > prices[i - 1]) {
                totalProfit += prices[i] - prices[i - 1];
            }
        }

        return totalProfit;
    }

    /* At most two transactions. */
    int maxProfitIII(const std::vector<int>& prices) {
        int buy1 = INT_MAX;
        int buy2 = INT_MAX;
        int sell1 = 0;
        int sell2 = 0;

        for (int price : prices) {
            buy1 = std::min(buy1, price);
            sell1 = std::max(sell1, price - buy1);

            // Effective cost of the second purchase after using
            // the profit from the first transaction.
            buy2 = std::min(buy2, price - sell1);
            sell2 = std::max(sell2, price - buy2);
        }

        return sell2;
    }

    /* At most K transactions. */
    int maxProfitIV(int k, const std::vector<int>& prices) {
        const int n = static_cast<int>(prices.size());
        if (k == 0 || n == 0) {
            return 0;
        }

        // k >= n / 2 is equivalent to unlimited transactions.
        if (k >= n / 2) {
            return maxProfitII(prices);
        }

        std::vector<std::vector<int>> dp(k + 1, std::vector<int>(n, 0));
        
        for (int transaction = 1; transaction <= k; ++transaction) {
            int effectiveBuy = prices[0];
            for (int day = 1; day < n; ++day) {
                effectiveBuy = std::min(effectiveBuy, prices[day] - dp[transaction - 1][day]);
                dp[transaction][day] = std::max(dp[transaction][day - 1], prices[day] - effectiveBuy);
            }
        }
        
        return dp[k][n - 1];
    }

    /* Cooldown */
    int maxProfitCooldown(const std::vector<int>& prices) {
        int buy = INT_MAX;
        int previousBuy = 0;
        int sell = 0;
        int previousSell = 0;

        for (int price : prices) {
            previousBuy = buy;
            buy = std::min(previousBuy, price - previousSell);

            previousSell = sell;
            sell = std::max(previousSell, price - previousBuy);
        }

        return sell;
    }

    /* Transaction Fee */
    int maxProfit(const std::vector<int>& prices, int fee) {
        if (prices.empty()) {
            return 0;
        }

        int buy = prices[0];
        int sell = 0;

        for (int i = 1; i < static_cast<int>(prices.size()); ++i) {
            buy = std::min(buy, prices[i] - sell);
            sell = std::max(sell, prices[i] - buy - fee);
        }

        return sell;
    }
}
