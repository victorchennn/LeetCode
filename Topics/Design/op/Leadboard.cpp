struct Product {
        int productId,
        int invectory,
        double costPrice
    }

    struct Order {
        int orderId,
        int productId,
        int quantity,
        double salePrice
    }

    profit = (salePrice - costPrice) * quantity // descending

    leaderboard:
        salePrice > costPrice &&
        quantity <= invectory



    unordered_map<int, Product> products_;
    unordered_map<int, Order> orders_;

    // 用于 product update 时找到所有受影响订单
    unordered_map<int, std::unordered_set<int>> productOrders_; // productId -> 所有相关的 orderId

    struct LeaderboardEntry {
        double profit;
        int orderId;
    };

    struct Compare {
        bool operator()(const LeaderboardEntry& a,
                        const LeaderboardEntry& b) const {
            if (a.profit != b.profit) {
                return a.profit > b.profit;
            }

            return a.orderId < b.orderId;
        }
    };

    std::set<LeaderboardEntry, Compare> leaderboard;
    
    class LeaderboardService {

        void onProductUpdate(const Product& product) {
            products_[product.productId] = product;

            auto it = productOrders_.find(product.productId);
            if (it == productOrders_.end()) {
                return;
            }

            for (int orderId : it->second) {
                evaluateOrder(orderId);
            }
            refreshDisplay();
        }

        void onOrderUpdate(const Order& order) {
            orders_[order.orderId] = order;
            productOrders_[order.productId].insert(order.orderId);

            if (products_.find(order.productId) != products_.end()) {
                const Product& product = it->second;
                double profit = (order.price - product.cost) * order.quantity;
                if (profit > 0 &&
                    order.quantity <= product.inventory) {
                    leaderboard.insert({profit, order.orderId});
                }
            } 
            // same as evaluateOrder(order.orderId);

            refreshDisplay();
        }

        bool evaluateOrder(int orderId) {
            const Order& order = orders.at(orderId);

            auto productIt = products.find(order.productId);
            if (productIt == products.end()) {
                return;
            }

            const Product& product = productIt->second;

            // 先删除旧排名
            auto oldProfitIt = orderToProfit.find(orderId);
            if (oldProfitIt != orderToProfit.end()) {
                double oldProfit = oldProfitIt->second;

                leaderboard.erase({oldProfit, orderId});
                orderToProfit.erase(oldProfitIt);
            }

            // 重新计算
            double profit = (order.price - product.cost) * order.quantity;
            bool profitable =
                profit > 0 &&
                order.quantity <= product.inventory;

            if (!profitable) {
                return;
            }

            // 插入新排名
            leaderboard.insert({profit, orderId});
            orderToProfit[orderId] = profit;
        }

        void refreshDisplay() {
            for (const auto& entry : leaderboard) {
                display(entry.orderId);
            }
        }
