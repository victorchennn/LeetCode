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
        std::map<int, std::list<Order>, std::greater<int>> bids_; // want to buy stocks 
        std::map<int, std::list<Order>> asks_; // sell stocks

        struct OrderLocation {
            Side side;
            int price;
            OrderQueue::iterator iterator;
        };
        std::unordered_map<std::int64_t, OrderLocation> orderIndex_; // orderId -> 买单还是卖单 所在价格 在该价格队列中的具体位置

        void addToBook(const Order& order) {
            if (order.side == Side::Buy) {
                auto& queue = bids_[order.price];
                queue.push_back(order);
                auto orderIt = std::prev(queue.end());
                orderIndex_[order.id] = {
                    Side::Buy,
                    order.price,
                    orderIt
                };
            } else {
                auto& queue = asks_[order.price];
                ............
            }
        }

        void matchOrder(Order& incoming) {
            if (incoming.side == Side::Buy) {
                while (incoming.quantity > 0 && !asks_.empty() && incoming.price >= asks_.begin()->first) {
                    auto bestAskIt = asks_.begin();
                    Order& restingOrder = bestAskIt->second.front();
    
                    int tradedQuantity = std::min(incoming.quantity, restingOrder.quantity);
                    incoming.quantity -= tradedQuantity;
                    restingOrder.quantity -= tradedQuantity;
    
                    if (restingOrder.quantity == 0) {
                        std::int64_t filledOrderId = restingOrder.id;
    
                        bestAskIt->second.pop_front();
                        orderIndex_.erase(filledOrderId);
    
                        if (bestAskIt->second.empty()) {
                            asks_.erase(bestAskIt);
                        }
                    }
                }
            } else {
                ... same 
            }
        }
                    
    public:
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
            
            auto indexIt = orderIndex_.find(orderId);
            if (indexIt == orderIndex_.end()) {
                return false;
            }

            OrderLocation location = indexIt->second;
            if (location.side == Side::Buy) {
                auto priceIt = bids_.find(location.price);
                priceIt->second.erase(location.iterator);
                if (priceIt->second.empty()) {
                    bids_.erase(priceIt);
                }
            } else {
                ......
            }

            orderIndex_.erase(indexIt);
            return true;
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
            cancelOrder(orderId); // this will cause deadloak, need to put real code here instead of calling 
            Order newOrder{orderId, side, newPrice, newQuantity};
            return addOrder(newOrder); // same
        }
};

// improve concurrency? add lock by price level
// 如果订单有 1000 万个？list / vector
// memory pool allocate first -> std::list<Order, MyAllocator<Order>>
