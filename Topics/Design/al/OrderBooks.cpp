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

void printResult(const vector<string>& result) {
    cout << "[";
    for (int i = 0; i < result.size(); ++i) {
        if (i > 0) cout << ", ";
        cout << "\"" << result[i] << "\"";
    }
    cout << "]\n";
}

void assertEqual(const vector<string>& actual,
                 const vector<string>& expected,
                 const string& testName) {
    if (actual == expected) {
        cout << testName << ": PASS\n";
    } else {
        cout << testName << ": FAIL\n";
        cout << "Expected: ";
        printResult(expected);
        cout << "Actual:   ";
        printResult(actual);
    }
}

int main() {
    {
        // Example 1:
        // b1 buys 5 at 100
        // s1 sells 2 at 90, matches b1 at resting price 100
        // s2 sells 4 at 100, matches remaining 3 with b1
        // b2 buys 1 at 101, matches remaining 1 of s2 at resting price 100
        Solution solution;

        vector<vector<string>> operations = {
            {"BUY",  "b1", "100", "5"},
            {"SELL", "s1", "90",  "2"},
            {"SELL", "s2", "100", "4"},
            {"BUY",  "b2", "101", "1"}
        };

        vector<string> expected = {
            "b1|s1|100|2",
            "b1|s2|100|3",
            "b2|s2|100|1"
        };

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 1: Example"
        );
    }

    {
        // No match:
        // highest bid 99 < lowest ask 100
        Solution solution;

        vector<vector<string>> operations = {
            {"BUY",  "b1", "99",  "5"},
            {"SELL", "s1", "100", "5"}
        };

        vector<string> expected = {};

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 2: No match"
        );
    }

    {
        // Exact full match
        Solution solution;

        vector<vector<string>> operations = {
            {"BUY",  "b1", "100", "5"},
            {"SELL", "s1", "100", "5"}
        };

        vector<string> expected = {
            "b1|s1|100|5"
        };

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 3: Exact full match"
        );
    }

    {
        // Incoming sell is partially filled.
        // Remaining sell quantity should stay on asks.
        Solution solution;

        vector<vector<string>> operations = {
            {"BUY",  "b1", "100", "3"},
            {"SELL", "s1", "90",  "5"},
            {"BUY",  "b2", "95",  "2"}
        };

        vector<string> expected = {
            "b1|s1|100|3",
            "b2|s1|90|2"
        };

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 4: Incoming sell partial fill"
        );
    }

    {
        // Incoming buy eats multiple sell price levels.
        Solution solution;

        vector<vector<string>> operations = {
            {"SELL", "s1", "98",  "2"},
            {"SELL", "s2", "99",  "3"},
            {"SELL", "s3", "100", "4"},
            {"BUY",  "b1", "100", "8"}
        };

        vector<string> expected = {
            "b1|s1|98|2",
            "b1|s2|99|3",
            "b1|s3|100|3"
        };

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 5: Buy eats multiple price levels"
        );
    }

    {
        // Incoming sell eats multiple bid price levels.
        // Highest bid should be matched first.
        Solution solution;

        vector<vector<string>> operations = {
            {"BUY",  "b1", "102", "2"},
            {"BUY",  "b2", "101", "3"},
            {"BUY",  "b3", "100", "4"},
            {"SELL", "s1", "100", "7"}
        };

        vector<string> expected = {
            "b1|s1|102|2",
            "b2|s1|101|3",
            "b3|s1|100|2"
        };

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 6: Sell eats multiple price levels"
        );
    }

    {
        // Same price level must respect FIFO.
        Solution solution;

        vector<vector<string>> operations = {
            {"BUY",  "b1", "100", "2"},
            {"BUY",  "b2", "100", "3"},
            {"BUY",  "b3", "100", "4"},
            {"SELL", "s1", "100", "6"}
        };

        vector<string> expected = {
            "b1|s1|100|2",
            "b2|s1|100|3",
            "b3|s1|100|1"
        };

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 7: FIFO at same price"
        );
    }

    {
        // Better price has priority over earlier order at worse price.
        Solution solution;

        vector<vector<string>> operations = {
            {"BUY",  "b1", "100", "5"},
            {"BUY",  "b2", "101", "2"},
            {"SELL", "s1", "100", "3"}
        };

        vector<string> expected = {
            "b2|s1|101|2",
            "b1|s1|100|1"
        };

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 8: Price priority before time priority"
        );
    }

    {
        // Buy limit is too low to reach the ask.
        Solution solution;

        vector<vector<string>> operations = {
            {"SELL", "s1", "101", "4"},
            {"BUY",  "b1", "100", "4"}
        };

        vector<string> expected = {};

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 9: Buy price below ask"
        );
    }

    {
        // Sell limit is too high to reach the bid.
        Solution solution;

        vector<vector<string>> operations = {
            {"BUY",  "b1", "100", "4"},
            {"SELL", "s1", "101", "4"}
        };

        vector<string> expected = {};

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 10: Sell price above bid"
        );
    }

    {
        // Resting order determines trade price.
        //
        // s1 rests at 90.
        // Incoming b1 is willing to pay up to 100.
        // Trade occurs at 90, not 100.
        Solution solution;

        vector<vector<string>> operations = {
            {"SELL", "s1", "90",  "3"},
            {"BUY",  "b1", "100", "3"}
        };

        vector<string> expected = {
            "b1|s1|90|3"
        };

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 11: Resting ask determines price"
        );
    }

    {
        // Resting bid determines trade price.
        //
        // b1 rests at 110.
        // Incoming s1 is willing to sell at 100.
        // Trade occurs at 110.
        Solution solution;

        vector<vector<string>> operations = {
            {"BUY",  "b1", "110", "3"},
            {"SELL", "s1", "100", "3"}
        };

        vector<string> expected = {
            "b1|s1|110|3"
        };

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 12: Resting bid determines price"
        );
    }

    {
        // Empty input
        Solution solution;

        vector<vector<string>> operations = {};
        vector<string> expected = {};

        assertEqual(
            solution.matchOrders(operations),
            expected,
            "Test 13: Empty input"
        );
    }

    return 0;
}



};
