// O(1) generate + O(n) space

class RandomGenerator {
public:
    explicit RandomGenerator(int n)
        : numbers_(n + 1),
          index_(0),
          rng_(std::random_device{}()) {
        std::iota(numbers_.begin(), numbers_.end(), 0); // 从 value 0 开始，依次递增 1，填满 [first, last)。
        std::shuffle(numbers_.begin(), numbers_.end(), rng_);
    }

    int generate() {
        if (index_ == numbers_.size()) {
            return -1;
        }

        return numbers_[index_++];
    }

private:
    std::vector<int> numbers_;
    std::size_t index_;
    std::mt19937 rng_;
};

// O(1) generate + O(k) space
// 使用 lazy Fisher–Yates shuffle。
// 不真正创建 [0, 1, ..., n]，而是用 unordered_map 只记录发生过的交换。

class RandomGenerator {
public:
    explicit RandomGenerator(int n)
        : remaining_(n + 1),
          rng_(std::random_device{}()) {}

    int generate() {
        if (remaining_ == 0) {
            return -1;
        }

        std::uniform_int_distribution<int> dist(0, remaining_ - 1);
        int randomIndex = dist(rng_);

        // randomIndex 当前位置实际存储的值
        int result = getValue(randomIndex);

        // 最后一个有效位置实际存储的值
        int lastIndex = remaining_ - 1;
        int lastValue = getValue(lastIndex);

        // 将最后一个值移动到 randomIndex
        mapping_[randomIndex] = lastValue;

        // lastIndex 之后不会再使用，可以删除
        mapping_.erase(lastIndex);

        --remaining_;
        return result;
    }

private:
    int getValue(int index) const {
        auto it = mapping_.find(index);

        if (it == mapping_.end()) {
            return index;
        }

        return it->second;
    }

    int remaining_;
    std::unordered_map<int, int> mapping_;
    std::mt19937 rng_;
};
