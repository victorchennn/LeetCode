class Solution {
public:
    int getNumberOfBacklogOrders(vector<vector<int>>& orders) {
        // Define pair type for (price, amount)
        using PriceAmount = pair<int, int>;
      
        // Min heap for sell orders (sorted by lowest price first)
        priority_queue<PriceAmount, vector<PriceAmount>, greater<PriceAmount>> sellOrders;
      
        // Max heap for buy orders (sorted by highest price first)
        priority_queue<PriceAmount> buyOrders;
      
        // Process each order
        for (auto& order : orders) {
            int price = order[0];
            int amount = order[1];
            int orderType = order[2];  // 0 for buy, 1 for sell
          
            if (orderType == 0) {  // Buy order
                // Match with existing sell orders while possible
                while (amount > 0 && !sellOrders.empty() && sellOrders.top().first <= price) {
                    auto [sellPrice, sellAmount] = sellOrders.top();
                    sellOrders.pop();
                  
                    if (amount >= sellAmount) {
                        // Buy order can fully consume this sell order
                        amount -= sellAmount;
                    } else {
                        // Sell order is partially consumed, push back remaining
                        sellOrders.push({sellPrice, sellAmount - amount});
                        amount = 0;
                    }
                }
              
                // Add remaining buy amount to backlog if any
                if (amount > 0) {
                    buyOrders.push({price, amount});
                }
              
            } else {  // Sell order
                // Match with existing buy orders while possible
                while (amount > 0 && !buyOrders.empty() && buyOrders.top().first >= price) {
                    auto [buyPrice, buyAmount] = buyOrders.top();
                    buyOrders.pop();
                  
                    if (amount >= buyAmount) {
                        // Sell order can fully consume this buy order
                        amount -= buyAmount;
                    } else {
                        // Buy order is partially consumed, push back remaining
                        buyOrders.push({buyPrice, buyAmount - amount});
                        amount = 0;
                    }
                }
              
                // Add remaining sell amount to backlog if any
                if (amount > 0) {
                    sellOrders.push({price, amount});
                }
            }
        }
      
        // Calculate total backlog orders
        long totalBacklog = 0;
      
        // Sum all remaining buy orders
        while (!buyOrders.empty()) {
            totalBacklog += buyOrders.top().second;
            buyOrders.pop();
        }
      
        // Sum all remaining sell orders
        while (!sellOrders.empty()) {
            totalBacklog += sellOrders.top().second;
            sellOrders.pop();
        }
      
        // Return result modulo 10^9 + 7
        const int MOD = 1e9 + 7;
        return totalBacklog % MOD;
    }
};
