enum class Side {BUY, SELL};

struct Order {
    int id;
    Side side;
    int price;
    int quantity;
};

class OrderBook {
    std::map<int, std::list<Order>, std::greater<int>> bids_;
    std::map<int, std::list<Order>> asks_;

    struct OrderLocation {
        Side side;
        int price;
        std::list<Order>::iterator iterator;
    };
    std::unordered_map<int, OrderLocation> orderIndex_;

    mutable std::mutex mutex_; 

public:
    bool addOrder(Order order) {
        std::lock_guard<std::mutex> lock(mutex_);
        return addOrderImpl(order);
    }
    
    bool cancelOrder(int orderId) {
        std::lock_guard<std::mutex> lock(mutex_);
        return cancelOrderImpl(orderId);
    }

    bool modifyOrder(int orderId, int newPrice, int newQuantity) {
        std::lock_guard<std::mutex> lock(mutex_);
        if (newPrice <= 0 || newQuantity <= 0) {
            return false;
        }

        auto indexIt = orderIndex_.find(orderId);
        if (indexIt == orderIndex_.end()) {
            return false;
        }

        Side side = indexIt->second.side;
        cancelOrderImpl(orderId);
        return addOrderImpl({orderId, side, newPrice, newQuantity});
    }

    std::optional<int> bestBid() const {
        std::lock_guard<std::mutex> lock(mutex_);
        if (bids_.empty()) {
            return nullopt;
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

private:
    bool addOrderImpl(Order& order) {
        if (order.id <= 0 || order.price <= 0 || order.quantity <= 0 || orderIndex_.contains(order.id)) [[unlikely]] {
            return false;
        }

        if (order.side == Side::BUY) {
            matchAgainst(order, asks_);
        } else {
            matchAgainst(order, bids_);
        }

        if (order.quantity > 0) {
            if (order.side == Side::BUY) {
                AddToBook(order, bids_);
            } else {
                AddToBook(order, asks_);
            }
        }
        return true;
    }

    bool cancelOrderImpl(int orderId) {
        auto orderIndexIt = orderIndex_.find(orderId);
        if (orderIndexIt == orderIndex_.end()) {
            return false;
        }

        OrderLocation location = orderIndexIt->second;
        if (location.side == Side::BUY) {
            eraseFromBook(location, bids_);
        } else {
            eraseFromBook(location, asks_);
        }

        orderIndex_.erase(orderIndexIt);
        return true;
    }

    template<typename Book>
    void AddToBook(Order& incoming, Book& book) {
        auto& orders = book[incoming.price];
        orders.push_back(incoming);
        orderIndex_[incoming.id] = {
            incoming.side, incoming.price, std::prev(orders.end())
        };
    }

    template<typename Book>
    void matchAgainst(Order& incoming, Book& book) {
        while (incoming.quantity > 0 && !book.empty()) {
            auto priceLevelIt = book.begin();
            int restingPrice = priceLevelIt->first;

            if ((incoming.side == Side::BUY && incoming.price < restingPrice) || 
            (incoming.side == Side::SELL && incoming.price > restingPrice)) {
                break;
            }

            std::list<Order>& ordersAtPrice = priceLevelIt->second;
            while (incoming.quantity > 0 && !ordersAtPrice.empty()) {
                Order& resting = ordersAtPrice.front();
                int tradedQuantity = min(resting.quantity, incoming.quantity);
                incoming.quantity -= tradedQuantity;
                resting.quantity -= tradedQuantity;

                if (resting.quantity == 0) {
                    orderIndex_.erase(resting.id);
                    ordersAtPrice.pop_front();
                }
            }

            if (ordersAtPrice.empty()) {
                book.erase(priceLevelIt);
            }
        }
    }

    template<typename Book>
    void eraseFromBook(OrderLocation& location, Book& book) {
        auto priceLevelIt = book.find(location.price);
        if (priceLevelIt == book.end()) {
            return;
        }

        priceLevelIt->second.erase(location.iterator);
        if (priceLevelIt->second.empty()) {
            book.erase(priceLevelIt);
        }
    }
};

/*
* Reverse layout:
* bids_: low -> high, best bid at back() bids = [97, 98, 99, 100]
* asks_: high -> low, best ask at back() asks = [103, 102, 101, 100]
*/
std::vector<PriceLevel> bids_;
std::vector<PriceLevel> asks_;
struct PriceLevel {
    int price;
    std::list<Order> orders;
    std::mutex mutex;
};

auto it = std::lower_bound(bids_.begin(), bids_.end(), price,
    [](const PriceLevel& level, int price) {
        return level.price < price;
    }
);

// integer index + intrusive linked list。
std::vector<OrderNode> pool(10'000'000);
struct OrderNode {
    int id;
    Side side;
    int price;
    int quantity;

    int prev = -1;
    int next = -1;
};
// 5 <-> 8 <-> 17 <-> 23
// pool[5] next = 8
// pool[8] prev = 5 next = 17
// pool[17] prev = 8 next = 23
// pool[23] prev = 17 next = -1

struct PriceLevel {
    int price;

    int head = -1;
    int tail = -1;
};

// 先用 perf 找瓶颈，看是 branch miss、cache miss 还是 backend bound，而不是凭感觉优化。
// 如果瓶颈在内存访问，优先考虑把价格档从树结构改成连续存储（例如固定价格范围的数组或 vector<PriceLevel>），提升 cache locality。
// 减少动态内存分配，订单对象使用 memory pool，而不是频繁 new/delete。
// 减少热路径中的分支，例如模板 + if constexpr、必要时使用 branchless 搜索，但前提是 benchmark 证明值得。
// 系统架构层面，采用 single-writer OrderBook，网络线程只负责收包，真正修改 OrderBook 的只有一个线程，通过共享内存或无锁队列把数据广播给多个策略，避免锁竞争。
// 网络层，使用 kernel bypass（如 DPDK、Onload）减少收发包延迟；如果需要极致延迟，部分风控或下单逻辑可以放到 FPGA。

// 网络线程只负责收包，真正修改OrderBook的只有一个线程 比“每个 PriceLevel 加 mutex”通常更像 trading system 的设计。

// SPSC RingBuffer
//  ↓
// OrderBook thread       ← single writer
//  ↓
// Book Update RingBuffer
//  ├── Strategy 1
//  ├── Strategy 2
//  └── Strategy 3

// Instead of making every individual data structure thread-safe, I would first try to eliminate shared mutation altogether.
// For a low-latency matching/order-book path, I'd prefer a single-writer architecture rather than putting locks on every price level.


class OrderPool {
private:
    vector<Order> pool;
    vector<int> freeList; // 哪些 index 现在没有被使用
    unordered_map<int, int> orderIndex_; // order id -> pool index

    map<int, PriceLevel, greater<int>> bids_;
    map<int, PriceLevel> asks_;

public:
    explicit OrderPool(int capacity)
        : pool(capacity) {

        // 先把所有空位置放进 free list
        for (int i = capacity - 1; i >= 0; --i) {
            freeList.push_back(i);
        }
    }

    // 从 pool 里面拿一个空位置
    int allocate(int id, int price, int quantity) {
        if (freeList.empty()) {
            return -1;
        }

        int index = freeList.back();
        freeList.pop_back();

        pool[index] = {id, price, quantity, -1, -1};
        return index;
    }

    // 归还位置
    void deallocate(int index) {
        freeList.push_back(index);
    }

    bool addOrder(int id, Side side, int price, int quantity) {
        if (orderIndex_.contains(id)) {
            return false;
        }

        int index = allocate(id, side, price, quantity);
        if (index == -1) {
            return false;
        }

        if (side == Side::BUY) {
            auto [it, inserted] = bids_.try_emplace(price, PriceLevel{price});
            pushBack(it->second, index);
        } else {
            auto [it, inserted] = asks_.try_emplace(price, PriceLevel{price});
            pushBack(it->second, index);
        }

        orderIndex_[id] = index;
        return true;
    }

    void pushBack(PriceLevel& level, int index) {
        Order& order = pool_[index];

        order.prev = level.tail;
        order.next = -1;

        // empty price level
        if (level.head == -1) {
            level.head = index;
            level.tail = index;
            return;
        }

        pool_[level.tail].next = index;
        level.tail = index;
    }

};

