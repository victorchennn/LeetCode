struct Cargo {
    std::int64_t id;
    int size;
};

struct Order {
    std::int64_t id;
    int shippingTime;
    std::vector<Cargo> cargos;
};

struct Plane {
    std::int64_t id;
    int departureTime;
    int remainingCapacity;
};

struct Allocation {
    Plane* plane;
    int size;
};

struct ProcessResult {
    bool accepted;
    std::vector<Allocation> allocations;
};

class Scheduler {
private:
    std::vector<Plane> planes_;

public:
    Scheduler(std::vector<Plane> planes)
        : planes_(std::move(planes)) {
        std::sort(
            planes_.begin(),
            planes_.end(),
            [](const Plane& a, const Plane& b) {
                return a.departureTime < b.departureTime;
            }
        );
    }

    ProcessResult processOrder(const Order& order) {
        // std::lock_guard<std::mutex> lock(mutex_); // or even add mutex for each plane
      
        int remainingOrderSize = 0;

        for (const Cargo& cargo : order.cargos) {
            remainingOrderSize += cargo.size;
        }

        std::vector<Allocation> allocations;
        for (Plane& plane : planes_) {
            if (plane.departureTime <= order.shippingTime || plane.remainingCapacity == 0) {
                continue;
            }

            int allocatedSize = std::min(remainingOrderSize, plane.remainingCapacity);
            allocations.push_back({ &plane, allocatedSize });

            remainingOrderSize -= allocatedSize;
            if (remainingOrderSize == 0) {
                break;
            }
        }

        if (remainingOrderSize > 0) { return {false, {}};}

        // 所有货物都能分配，才真正更新状态
        for (const auto& allocation : allocations) {
            allocation.plane->remainingCapacity -= allocation.size;
        }

        return {true, allocations};
    }
};

// 货物不能拆 sort cargos by descending size and place each cargo into the earliest plane with enough remaining capacity (First Fit Decreasing)
// 如果每天有一百万个订单 std::map<departureTime, vector<Plane>> upper_bound(order.shippingTime)
// 飞机会动态增加怎么办 std::map<int, vector<Plane>>
// 多线程怎么办？
