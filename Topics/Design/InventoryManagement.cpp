addStock(warehouseId, productId, quantity)
removeStock(warehouseId, productId, quantity)
getAvailableWarehouses(productId, quantity)
transferStock(fromWarehouseId, toWarehouseId, productId, quantity)
setLowStockThreshold(warehouseId, productId, threshold)


class AlertListener {
public:
    virtual ~AlertListener() = default;

    virtual void onLowStock(
        const std::string& warehouseId,
        const std::string& productId,
        int quantity
    ) = 0;
};

class ConsoleAlert : public AlertListener {
public:
    void onLowStock(
        const std::string& warehouseId,
        const std::string& productId,
        int quantity
    ) override {
        std::cout << warehouseId << " " << productId
                  << " low stock: " << quantity << '\n';
    }
};

class Warehouse {
public:
    explicit Warehouse(std::string id)
        : id_(std::move(id)) {}

    void addStock(const std::string& productId, int quantity) {
        std::lock_guard<std::mutex> lock(mutex_);
        stock_[productId] += quantity;
    }

    bool removeStock(
        const std::string& productId,
        int quantity,
        int& newQuantity,
        bool& shouldAlert
    ) {
        std::lock_guard<std::mutex> lock(mutex_);

        auto it = stock_.find(productId);

        if (quantity <= 0 ||
            it == stock_.end() ||
            it->second < quantity) {
            return false;
        }

        int oldQuantity = it->second;
        it->second -= quantity;
        newQuantity = it->second;

        auto thresholdIt = thresholds_.find(productId);

        shouldAlert =
            thresholdIt != thresholds_.end() &&
            oldQuantity >= thresholdIt->second &&
            newQuantity < thresholdIt->second;

        return true;
    }

    int getStock(const std::string& productId) const {
        std::lock_guard<std::mutex> lock(mutex_);

        auto it = stock_.find(productId);
        return it == stock_.end() ? 0 : it->second;
    }

    void setThreshold(
        const std::string& productId,
        int threshold
    ) {
        std::lock_guard<std::mutex> lock(mutex_);
        thresholds_[productId] = threshold;
    }

private:
    friend class InventoryManager;

    std::string id_;
    std::unordered_map<std::string, int> stock_;
    std::unordered_map<std::string, int> thresholds_;
    mutable std::mutex mutex_;
};

class InventoryManager {
public:
    explicit InventoryManager(
        std::shared_ptr<AlertListener> listener = nullptr
    ) : listener_(std::move(listener)) {}

    bool addWarehouse(const std::string& warehouseId) {
        if (warehouses_.contains(warehouseId)) {
            return false;
        }

        warehouses_[warehouseId] = std::make_shared<Warehouse>(warehouseId);
        return true;
    }

    bool addStock(const std::string& warehouseId, const std::string& productId, int quantity) {
        auto warehouse = getWarehouse(warehouseId);
        if (!warehouse || quantity <= 0) {
            return false;
        }

        warehouse->addStock(productId, quantity);
        return true;
    }

    bool removeStock(const std::string& warehouseId, const std::string& productId, int quantity) {
        auto warehouse = getWarehouse(warehouseId);
        if (!warehouse) {
            return false;
        }

        int newQuantity = 0;
        bool shouldAlert = false;
        if (!warehouse->removeStock(productId, quantity, newQuantity, shouldAlert)) {
            return false;
        }

        // Alert callback 放在 Warehouse 锁外执行
        if (shouldAlert && listener_) {
            listener_->onLowStock(warehouseId, productId, newQuantity);
        }
        return true;
    }

    int getStock(const std::string& warehouseId, const std::string& productId) const {
        auto warehouse = getWarehouse(warehouseId);
        if (!warehouse) {
            return 0;
        }

        return warehouse->getStock(productId);
    }

    bool setThreshold(const std::string& warehouseId, const std::string& productId, int threshold) {
        auto warehouse = getWarehouse(warehouseId);
        if (!warehouse || threshold < 0) {
            return false;
        }

        warehouse->setThreshold(productId, threshold);
        return true;
    }

    std::vector<std::string> getAvailableWarehouses(const std::string& productId, int requiredQuantity) const {
        std::vector<std::string> result;

        if (requiredQuantity <= 0) {
            return result;
        }

        for (const auto& [warehouseId, warehouse] : warehouses_) {
            if (warehouse->getStock(productId) >= requiredQuantity) {
                result.push_back(warehouseId);
            }
        }
        return result;
    }

    bool transferStock(const std::string& fromWarehouseId, const std::string& toWarehouseId, const std::string& productId, int quantity) {
        if (fromWarehouseId == toWarehouseId || quantity <= 0) {
            return false;
        }

        auto from = getWarehouse(fromWarehouseId);
        auto to = getWarehouse(toWarehouseId);

        if (!from || !to) {
            return false;
        }

        int newQuantity = 0;
        bool shouldAlert = false;

        {
            // 一次锁住两个 Warehouse，避免死锁
            std::scoped_lock lock(from->mutex_, to->mutex_);

            auto it = from->stock_.find(productId);
            if (it == from->stock_.end() ||
                it->second < quantity) {
                return false;
            }

            int oldQuantity = it->second;
            it->second -= quantity;
            to->stock_[productId] += quantity;
            newQuantity = it->second;

            auto thresholdIt = from->thresholds_.find(productId);
            if (thresholdIt != from->thresholds_.end()) {
                int threshold = thresholdIt->second;
                shouldAlert = oldQuantity >= threshold && newQuantity < threshold;
            }
        }

        // 不要在 Warehouse 锁内调用外部 listener
        if (shouldAlert && listener_) {
            listener_->onLowStock(fromWarehouseId, productId, newQuantity);
        }
        return true;
    }

private:
    std::shared_ptr<Warehouse> getWarehouse(const std::string& warehouseId) const {
        auto it = warehouses_.find(warehouseId);
        if (it == warehouses_.end()) {
            return nullptr;
        }

        return it->second;
    }

    std::unordered_map<std::string, std::shared_ptr<Warehouse>> warehouses_;
    std::shared_ptr<AlertListener> listener_;
};




















