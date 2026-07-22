enum class Side {Buy, Sell}; // Strongly Typed Enum

    struct Order {
        std::int64_t id;
        Side side;
        int price;
        int quantity;
    }

    class OrderBook {

        private:
            std::mutex mutex_;

            using OrderQueue = std::list<Order>; // std::vector if reallocate fail

            std::map<int, OrderQueue, std::greater<int>> bids_; // buy stocks
            std::map<int, OrderQueue> asks_; // sell stocks

            struct OrderLocation {
                Side side;
                int price;
                OrderQueue::iterator iterator;
            };

            std::unordered_map<std::int64_t, OrderLocation> orderIndex_;

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
                    queue.push_back(order);
                    auto orderIt = std::prev(queue.end());
                    orderIndex_[order.id] = {
                        Side::Sell,
                        order.price,
                        orderIt
                    };
                }
            }

            void matchBuyOrder(Order& incoming) {
                while (
                    incoming.quantity > 0 &&
                    !asks_.empty() &&
                    incoming.price >= asks_.begin()->first
                ) {
                    auto bestAskIt = asks_.begin();
                    Order& restingOrder = bestAskIt->second.front();

                    int tradedQuantity =
                        std::min(incoming.quantity, restingOrder.quantity);

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
            }

            void matchSellOrder(Order& incoming) {
                while (
                    incoming.quantity > 0 &&
                    !bids_.empty() &&
                    incoming.price <= bids_.begin()->first
                ) {
                    auto bestBidIt = bids_.begin();
                    Order& restingOrder = bestBidIt->second.front();

                    int tradedQuantity =
                        std::min(incoming.quantity, restingOrder.quantity);

                    incoming.quantity -= tradedQuantity;
                    restingOrder.quantity -= tradedQuantity;

                    if (restingOrder.quantity == 0) {
                        std::int64_t filledOrderId = restingOrder.id;

                        bestBidIt->second.pop_front();
                        orderIndex_.erase(filledOrderId);

                        if (bestBidIt->second.empty()) {
                            bids_.erase(bestBidIt);
                        }
                    }
                }
            }

            void matchOrder(Order& incoming) {
                if (incoming.side == Side::Buy) {
                    matchBuyOrder(incoming);
                } else {
                    matchSellOrder(incoming);
                }
            }
                
                
        public:
            void addOrder(Order order) {
                std::lock_guard<std::mutex> lock(mutex_);
                
                if (order.id <= 0 ||
                    order.price <= 0 ||
                    order.quantity <= 0
                ) {
                    return false;
                }

                if (orderIndex_.find(order.id) != orderIndex_.end()) {
                    return false;
                }

                matchOrder(order);

                if (order.quantity > 0) {
                    addToBook(order);
                }

                return true;
            }

            bool cancelOrder(std::int64_t orderId) {
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
                    auto priceIt = asks_.find(location.price);

                    priceIt->second.erase(location.iterator);

                    if (priceIt->second.empty()) {
                        asks_.erase(priceIt);
                    }
                }

                orderIndex_.erase(indexIt);

                return true;
            }

            bool modifyOrder(std::int64_t orderId, int newPrice, int newQuantity) {
                if (newPrice <= 0 || newQuantity <= 0) {
                    return false;
                }

                auto indexIt = orderIndex_.find(orderId);
                if (indexIt == orderIndex_.end()) {
                    return false;
                }

                Side side = indexIt->second.side;

                cancelOrder(orderId);

                Order newOrder{
                    orderId,
                    side,
                    newPrice,
                    newQuantity
                };

                return addOrder(newOrder);
            }


    };
