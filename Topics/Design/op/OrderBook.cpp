enum class Side {Buy, Sell}; // Strongly Typed Enum

struct Order {
    std::int64_t id;
    Side side;
    int price;
    int quantity;
};

class OrderBook {
    private:
        std::mutex mutex_; 
        // why list? O(1) 删除任意订单 iterator不会因为插入而失效
        // price -> all the orders for that price
        std::map<int, std::list<Order>, std::greater<int>> bids_; // want to buy stocks 
        std::map<int, std::list<Order>> asks_; // sell stocks

        using OrderQueue = std::list<Order>;
         /*
         * Reverse layout:
         * bids_: low -> high, best bid at back() bids = [97, 98, 99, 100]
         * asks_: high -> low, best ask at back() asks = [103, 102, 101, 100]
         */
        // std::vector<PriceLevel> bids_;
        // std::vector<PriceLevel> asks_;
        // struct PriceLevel {
        //     int price;
        //     OrderQueue orders;
        // };

        // cancel purpose
        struct OrderLocation {
            Side side;
            int price;
            OrderQueue::iterator iterator;
        };
        std::unordered_map<std::int64_t, OrderLocation> orderIndex_; // orderId -> 买单还是卖单 所在价格 在该价格队列中的具体位置

        void addToBook(const Order& order) {
            if (order.side == Side::Buy) { //  std::less<int>{} 
                auto& queue = bids_[order.price];
                queue.push_back(order);
                auto orderIt = std::prev(queue.end());
                orderIndex_[order.id] = {
                    Side::Buy,
                    order.price,
                    orderIt
                };
            } else { // std::greater<int>{}
                auto& queue = asks_[order.price];
                ............
            }
        }

        void matchOrder(Order& incoming) {
            if (incoming.side == Side::Buy) {
                while (incoming.quantity > 0 && !asks_.empty() && incoming.price >= asks_.begin()->first) { // asks_.back().price
                    auto bestAskIt = asks_.begin(); //  PriceLevel& bestAsk = asks_.back();
                    Order& restingOrder = bestAskIt->second.front(); // Order& resting = bestAsk.orders.front();
    
                    int tradedQuantity = std::min(incoming.quantity, restingOrder.quantity);
                    incoming.quantity -= tradedQuantity;
                    restingOrder.quantity -= tradedQuantity;
    
                    if (restingOrder.quantity == 0) {
                        std::int64_t filledOrderId = restingOrder.id;
    
                        bestAskIt->second.pop_front(); // bestAsk.orders.pop_front();
                        orderIndex_.erase(filledOrderId);
    
                        if (bestAskIt->second.empty()) {
                            asks_.erase(bestAskIt);  // asks_.pop_back();
                        }
                    }
                }
            } else {
                ... same 
            }
        }

        bool cancelOrder(std::int64_t orderId) {
            auto indexIt = orderIndex_.find(orderId);
            if (indexIt == orderIndex_.end()) {
                return false;
            }
    
            const OrderLocation location = indexIt->second;
            if (location.side == Side::Buy) {
                auto levelIt = findLevel(bids_, location.price, std::less<int>{});
                if (levelIt == levels.end() || levelIt->price != location.price) {
                    return false;
                }
                levelIt->orders.erase(location.orderIt);
                if (levelIt->orders.empty()) {
                    levels.erase(levelIt);
                }
            } else {
                ....
            }
    
            orderIndex_.erase(indexIt);
            return true;
        }

        template <typename Compare>
        static auto findLevel(std::vector<PriceLevel>& levels,
                              int price,
                              Compare compare) {
            return std::lower_bound(
                levels.begin(),
                levels.end(),
                price,
                [compare](const PriceLevel& level, int targetPrice) {
                    return compare(level.price, targetPrice);
                }
            );
        }
                    
    public:
        // explicit OrderBook(std::size_t expectedPriceLevels = 256,
        //                std::size_t expectedOrders = 4096) {
        //     bids_.reserve(expectedPriceLevels);
        //     asks_.reserve(expectedPriceLevels);
        //     orderIndex_.reserve(expectedOrders);
        // }

        bool addOrder(Order order) {
            std::lock_guard<std::mutex> lock(mutex_);
            
            if (order.id <= 0 || order.price <= 0 || order.quantity <= 0) { return false; }
            if (orderIndex_.find(order.id) != orderIndex_.end()) { return false; }

            matchOrder(order);
            if (order.quantity > 0) {
                addToBook(order);
            }
            return true;
        }

        bool cancelOrder(std::int64_t orderId) {
            std::lock_guard<std::mutex> lock(mutex_);
            return cancel(orderId);
        }


        bool modifyOrder(std::int64_t orderId, int newPrice, int newQuantity) {
            std::lock_guard<std::mutex> lock(mutex_);
            
            if (newPrice <= 0 || newQuantity <= 0) {
                return false;
            }

            auto indexIt = orderIndex_.find(orderId);
            if (indexIt == orderIndex_.end()) {
                return false;
            }
            
            Side side = indexIt->second.side;
            cancelOrder(orderId); 
            Order newOrder{orderId, side, newPrice, newQuantity};
            matchOrder(newOrder);
            if (order.quantity > 0) {
                addToBook(newOrder);
            }
            return true;
        }

         std::optional<int> bestBid() const {
            std::lock_guard<std::mutex> lock(mutex_);
    
            if (bids_.empty()) {
                return std::nullopt;
            }
            return bids_.back().price;
        }
    
        std::optional<int> bestAsk() const {
            std::lock_guard<std::mutex> lock(mutex_);
    
            if (asks_.empty()) {
                return std::nullopt;
            }
            return asks_.back().price;
        }
};

// improve concurrency? add lock by price level
// 如果订单有 1000 万个？list / vector
// memory pool allocate first -> std::list<Order, MyAllocator<Order>>
