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
            std::list<Order>::iterator iterator;
        };
        std::unordered_map<std::int64_t, OrderLocation> orderIndex_; // orderId -> 买单还是卖单 所在价格 在该价格队列中的具体位置

        bool addOrderUnlocked(Order order) {
            if (order.id <= 0 || order.price <= 0 || order.quantity <= 0 || orderIndex_.contains(order.id)) [[unlikely]] {
                return false;
            }
    
            if (order.side == Side::Buy) {
                matchAgainst<Side::Buy>(order, asks_);
            } else {
                matchAgainst<Side::Sell>(order, bids_);
            }
    
            if (order.quantity > 0) {
                if (order.side == Side::Buy) {
                    addToBook(bids_, order);
                } else {
                    addToBook(asks_, order);
                }
            }
            return true;
        }

        template <typename Book>
        void addToBook(Book& book, const Order& order) {
            std::list<Order>& ordersAtPrice = book[order.price];
            ordersAtPrice.push_back(order);
            orderIndex_[order.id] = {
                order.side,
                order.price,
                std::prev(ordersAtPrice.end())
            };
        }

        template <Side side, typename Book>
        void matchAgainst(Order& incoming, Book& oppositeBook) {
            while (incoming.quantity > 0 && !oppositeBook.empty()) {
                auto priceLevelIt = oppositeBook.begin();
                int restingPrice = priceLevelIt->first;
    
                if constexpr (side == Side::Buy) {
                    if (incoming.price < restingPrice) {
                        break;
                    }
                } else {
                    if (incoming.price > restingPrice) {
                        break;
                    }
                }
    
                std::list<Order>& ordersAtPrice = priceLevelIt->second;
    
                while (incoming.quantity > 0 && !ordersAtPrice.empty()) {
                    Order& resting = ordersAtPrice.front();
                    int tradedQuantity = std::min(incoming.quantity, resting.quantity);
    
                    incoming.quantity -= tradedQuantity;
                    resting.quantity -= tradedQuantity;
    
                    if (resting.quantity == 0) {
                        orderIndex_.erase(resting.id);
                        ordersAtPrice.pop_front();
                    }
                }
    
                if (ordersAtPrice.empty()) {
                    oppositeBook.erase(priceLevelIt);
                }
            }
        }

        bool cancelOrder(std::int64_t orderId) {
            auto orderIndexIt = orderIndex_.find(orderId);
            if (orderIndexIt == orderIndex_.end()) {
                return false;
            }
    
            OrderLocation location = orderIndexIt->second;
            if (location.side == Side::Buy) {
                eraseFromBook(bids_, location);
            } else {
                eraseFromBook(asks_, location);
            }
    
            orderIndex_.erase(orderIndexIt);
            return true;
        }

        template <typename Book>
        void eraseFromBook(Book& book, const OrderLocation& location) {
            auto priceLevelIt = book.find(location.price);
            if (priceLevelIt == book.end()) {
                return;
            }
    
            priceLevelIt->second.erase(location.orderIt);
            if (priceLevelIt->second.empty()) {
                book.erase(priceLevelIt);
            }
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
            return addOrderUnlocked(std::move(order));
        }

        bool cancelOrder(std::int64_t orderId) {
            std::lock_guard<std::mutex> lock(mutex_);
            return cancelOrderUnlocked(orderId);
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
            cancelOrderUnlocked(orderId);
            return addOrderUnlocked({ orderId, side, newPrice, newQuantity });
        }

         std::optional<int> bestBid() const {
            std::lock_guard<std::mutex> lock(mutex_);
    
            if (bids_.empty()) {
                return std::nullopt;
            }
            return bids_.begin()->first;
        }
    
        std::optional<int> bestAsk() const {
            std::lock_guard<std::mutex> lock(mutex_);
    
            if (asks_.empty()) {
                return std::nullopt;
            }
            return asks_.begin()->first;
        }
};

// improve concurrency? add lock by price level
// 如果订单有 1000 万个？list / vector
// memory pool allocate first -> std::list<Order, MyAllocator<Order>>


先用 perf 找瓶颈，看是 branch miss、cache miss 还是 backend bound，而不是凭感觉优化。
如果瓶颈在内存访问，优先考虑把价格档从树结构改成连续存储（例如固定价格范围的数组或 vector<PriceLevel>），提升 cache locality。
减少动态内存分配，订单对象使用 memory pool，而不是频繁 new/delete。
减少热路径中的分支，例如模板 + if constexpr、必要时使用 branchless 搜索，但前提是 benchmark 证明值得。
系统架构层面，采用 single-writer OrderBook，网络线程只负责收包，真正修改 OrderBook 的只有一个线程，通过共享内存或无锁队列把数据广播给多个策略，避免锁竞争。
网络层，使用 kernel bypass（如 DPDK、Onload）减少收发包延迟；如果需要极致延迟，部分风控或下单逻辑可以放到 FPGA。
