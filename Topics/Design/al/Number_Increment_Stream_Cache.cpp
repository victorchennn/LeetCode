class FixedCache {
private:
    std::vector<int> data_;
    int capacity_;
    int head_ = 0;  // 最老数据的位置
    int size_ = 0;

    // 获取逻辑上的第 index 个元素
    int get(int index) const {
        return data_[(head_ + index) % capacity_];
    }

public:
    explicit FixedCache(int capacity)
        : data_(capacity), capacity_(capacity) {
        if (capacity <= 0) {
            throw std::invalid_argument("capacity must be positive");
        }
    }

    void insert(int value) {
        // 缓存还没满
        if (size_ < capacity_) {
            int position = (head_ + size_) % capacity_;
            data_[position] = value;
            ++size_;
            return;
        }

        // 缓存已满：覆盖最老的数据
        data_[head_] = value;
        head_ = (head_ + 1) % capacity_;
    }

    // 返回缓存中严格小于 threshold 的最大值
    std::optional<int> query(int threshold) const {
        int left = 0;
        int right = size_;

        // 找第一个 >= threshold 的位置
        while (left < right) {
            int mid = left + (right - left) / 2;

            if (get(mid) < threshold) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        // left 是第一个 >= threshold 的位置
        // 所以 left - 1 是最后一个 < threshold 的位置
        if (left == 0) {
            return std::nullopt;
        }

        return get(left - 1);
    }
};
