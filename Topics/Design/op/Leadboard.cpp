struct Product {
    int productId;
    int inventory;
    double costPrice;
};

struct Order {
    int orderId;
    int productId;
    int quantity;
    double salePrice;
};

struct LeaderboardEntry {
    double profit;
    int orderId;
};

struct Compare {
    bool operator()(const LeaderboardEntry& a, const LeaderboardEntry& b) const {
        if (a.profit != b.profit) {
            return a.profit > b.profit;
        }
        return a.orderId < b.orderId;
    }
};

class LeaderboardService {
private:
    unordered_map<int, Product> products_;
    unordered_map<int, Order> orders_;

    // productId -> all orders using this product
    unordered_map<int, unordered_set<int>> productOrders_;

    set<LeaderboardEntry, Compare> leaderboard_;

    // orderId -> current profit
    unordered_map<int, double> orderToProfit_;

public:
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
        // If order already existed and product changed, remove dependency from old product.
        auto oldIt = orders_.find(order.orderId);
        if (oldIt != orders_.end()) {
            int oldProductId = oldIt->second.productId;

            if (oldProductId != order.productId) {
                productOrders_[oldProductId].erase(order.orderId);
            }
        }

        orders_[order.orderId] = order;
        productOrders_[order.productId].insert(order.orderId);

        evaluateOrder(order.orderId);
        refreshDisplay();

        // vector<LeaderboardEntry> snapshot;
        // {
        //     lock_guard<mutex> lock(mutex_);
    
        //     updateOrder(order);
        //     evaluateOrder(order.orderId);
    
        //     snapshot = getTopKLocked();
        // }
        // // mutex already released
        // display(snapshot);
    }
        
    void onOrderDelete(int orderId) {
        auto orderIt = orders_.find(orderId);
        if (orderIt == orders_.end()) {
            return;
        }
    
        const Order& order = orderIt->second;
    
        // 1. remove from leaderboard if currently profitable
        auto profitIt = orderToProfit_.find(orderId);
        if (profitIt != orderToProfit_.end()) {
            leaderboard_.erase({
                profitIt->second,
                orderId
            });
    
            orderToProfit_.erase(profitIt);
        }
    
        // 2. remove product -> order dependency
        productOrders_[order.productId].erase(orderId);
    
        // 3. remove order itself
        orders_.erase(orderIt);
        refreshDisplay();
    }

private:
    void evaluateOrder(int orderId) {
        auto orderIt = orders_.find(orderId);
        if (orderIt == orders_.end()) {
            return;
        }

        const Order& order = orderIt->second;

        // Remove old leaderboard entry first
        auto oldProfitIt = orderToProfit_.find(orderId);
        if (oldProfitIt != orderToProfit_.end()) {
            leaderboard_.erase({oldProfitIt->second, orderId});
            orderToProfit_.erase(oldProfitIt);
        }

        auto productIt = products_.find(order.productId);
        if (productIt == products_.end()) {
            return;
        }

        const Product& product = productIt->second;
        double profit = (order.salePrice - product.costPrice) * order.quantity;
        if (profit <= 0 || order.quantity > product.inventory) {
            return;
        }

        leaderboard_.insert({profit, orderId});
        orderToProfit_[orderId] = profit;
    }

    void refreshDisplay() {
        for (const auto& entry : leaderboard_) {
            display(entry.orderId);
        }
    }

    void display(int orderId) {
        // provided API
    }
};

// 为什么不用 priority_queue？
// update arbitrary order / remove arbitrary order
// pq no erase(orderId), no find(orderId)

// How do you handle stale or out-of-order events?
// Use monotonically increasing sequence numbers or versions per entity,
// and ignore updates older than the latest processed version.
