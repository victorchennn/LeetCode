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
    
    private:
        friend class InventoryManager;
    
        std::string id_;
        std::unordered_map<std::string, int> stock_;
        std::unordered_map<std::string, int> thresholds_;
        std::mutex mutex_;
};

class InventoryManager {
public:
    explicit InventoryManager(
        std::shared_ptr<AlertListener> listener
    ) : listener_(std::move(listener)) {}

    void addWarehouse(const std::string& warehouseId) {
        warehouses_[warehouseId] = std::make_shared<Warehouse>(warehouseId);
    }

    bool addStock(const std::string& warehouseId, const std::string& productId, int quantity) {
        auto warehouse = getWarehouse(warehouseId);
        if (!warehouse || quantity <= 0) {
            return false;
        }

        std::lock_guard<std::mutex> lock(warehouse->mutex_);
        warehouse->stock_[productId] += quantity;
        return true;
    }

    bool removeStock(const std::string& warehouseId, const std::string& productId, int quantity) {
        auto warehouse = getWarehouse(warehouseId);
        if (!warehouse || quantity <= 0) {
            return false;
        }

        int newQuantity;
        bool shouldAlert = false;

        {
            std::lock_guard<std::mutex> lock(warehouse->mutex_);

            auto it = warehouse->stock_.find(productId);
            if (it == warehouse->stock_.end() || it->second < quantity) {
                return false;
            }

            int oldQuantity = it->second;
            it->second -= quantity;
            newQuantity = it->second;

            auto thresholdIt = warehouse->thresholds_.find(productId);
            if (thresholdIt != warehouse->thresholds_.end()) {
                int threshold = thresholdIt->second;

                shouldAlert =
                    oldQuantity >= threshold &&
                    newQuantity < threshold;
            }
        }

        // 外部回调放在锁外
        if (shouldAlert && listener_) {
            listener_->onLowStock(warehouseId, productId,newQuantity);
        }

        return true;
    }

    bool transferStock(const std::string& fromId, const std::string& toId, const std::string& productId, int quantity) {
        auto from = getWarehouse(fromId);
        auto to = getWarehouse(toId);

        if (!from || !to || from == to || quantity <= 0) {
            return false;
        }

        std::scoped_lock lock(from->mutex_, to->mutex_);

        auto it = from->stock_.find(productId);
        if (it == from->stock_.end() ||
            it->second < quantity) {
            return false;
        }

        it->second -= quantity;
        to->stock_[productId] += quantity;

        return true;
    }

    void setThreshold(const std::string& warehouseId, const std::string& productId, int threshold) {
        auto warehouse = getWarehouse(warehouseId);
        if (!warehouse || threshold < 0) {
            return;
        }

        std::lock_guard<std::mutex> lock(warehouse->mutex_);
        warehouse->thresholds_[productId] = threshold;
    }

    int getStock(const std::string& warehouseId, const std::string& productId) {
        auto warehouse = getWarehouse(warehouseId);

        if (!warehouse) {
            return 0;
        }

        std::lock_guard<std::mutex> lock(warehouse->mutex_);

        auto it = warehouse->stock_.find(productId);
        return it == warehouse->stock_.end() ? 0 : it->second;
    }

private:
    std::shared_ptr<Warehouse> getWarehouse(
        const std::string& warehouseId
    ) {
        auto it = warehouses_.find(warehouseId);

        return it == warehouses_.end()
            ? nullptr
            : it->second;
    }

    std::unordered_map<
        std::string,
        std::shared_ptr<Warehouse>
    > warehouses_;

    std::shared_ptr<AlertListener> listener_;
};





















