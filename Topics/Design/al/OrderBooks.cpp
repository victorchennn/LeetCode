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

// Example 1
// operations = [["BUY","b1","100","5"],["SELL","s1","90","2"],["SELL","s2","100","4"],["BUY","b2","101","1"]]
// return = ["b1|s1|100|2","b1|s2|100|3","b2|s2|100|1"]

class Solution {

struct Order {
    string id;
    long long quantity;
};

map<long long, deque<Order>, greater<long long>> bids_; // Highest buy price first
map<long long, deque<Order>> asks_; // Lowest sell price first

public:
  vector<string> matchOrders(vector<vector<string>> operations) {
        vector<string> trades;
        for (const auto& operation : operations) {
            const string& side = operation[0];
            const string& orderId = operation[1];
            long long price = stoll(operation[2]);
            long long quantity = stoll(operation[3]);

            if (side == "BUY") {
                processBuy(orderId, price, quantity, trades);
            } else {
                processSell(orderId, price, quantity, trades);
            }
        }
        return trades;
  }

private:
  void processBuy(const string& buyId, long long price, long long quantity, vector<string>& trades) {
        while (quantity > 0 && !asks_.empty() && asks_.begin()->first <= price) {
            auto priceLevelIt = asks_.begin();
            long long sellPrice = priceLevelIt->first;
            auto& orders = priceLevelIt->second;

            Order& current = orders.front();
            long long tradeQuantity = min(quantity, current.quantity);

            trades.push_back(buyId + "|" + current.id + "|" + 
                  to_string(sellPrice) + "|" + to_string(tradeQuantity));
            
            quantity -= tradeQuantity;
            current.quantity -= tradeQuantity;

            if (current.quantity == 0) {
                orders.pop_front();
            }
            if (orders.empty()) {
                asks_.erase(priceLevelIt);
            }
         }
         if (quantity > 0) {
            bids_[price].push_back({buyId, quantity});
         }
  }

  void processSell(const string& sellId, long long price, long long quantity, vector<string>& trades) {
        while (quantity > 0 && !bids_.empty() && bids_.begin()->first >= price) {
            auto priceLevelIt = bids_.begin();
            long long buyPrice = priceLevelIt->first;
            auto& orders = priceLevelIt->second;

            Order& current = orders.front();
            long long tradeQuantity = min(quantity, current.quantity);

            trades.push_back(current.id + "|" + sellId + "|" + 
                  to_string(buyPrice) + "|" + to_string(tradeQuantity));
            
            quantity -= tradeQuantity;
            current.quantity -= tradeQuantity;

            if (current.quantity == 0) {
                orders.pop_front();
            }
            if (orders.empty()) {
                bids_.erase(priceLevelIt);
            }
         }
         if (quantity > 0) {
            asks_[price].push_back({sellId, quantity});
         }
  }



};
