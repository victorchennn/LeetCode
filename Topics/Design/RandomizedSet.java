// multiple same values? unordered_map<int, unordered_set<int>> index_;
// values = [5,5,5,3] index:
// 5 -> {0,1,2}
// 3 -> {3}

class RandomizedSet {
private:
    std::vector<int> values_;
    std::unordered_map<int, std::size_t> index_; // value -> position

    std::mt19937 generator_{std::random_device{}()};

public:
    RandomizedSet() = default;

    bool insert(int val) {
        if (index_.contains(val)) {
            return false;
        }

        index_[val] = values_.size();
        values_.push_back(val);
        return true;
    }

    bool remove(int val) {
        auto it = index_.find(val);
        if (it == index_.end()) {
            return false;
        }

        std::size_t removeIndex = it->second;
        std::size_t lastIndex = values_.size() - 1;
        int lastValue = values_.back();

        // 用最后一个元素覆盖要删除的位置
        if (removeIndex != lastIndex) {
            values_[removeIndex] = lastValue;
            index_[lastValue] = removeIndex;
        }

        values_.pop_back();
        index_.erase(it);

        return true;
    }

    int getRandom() {
        std::uniform_int_distribution<std::size_t> distribution(
            0, values_.size() - 1
        );

        return values_[distribution(generator_)];
    }
};
