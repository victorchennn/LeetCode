#include <random>
#include <unordered_map>
#include <vector>

class RandomizedSet {
private:
    std::vector<int> values_;
    // unordered_map<int, unordered_set<int>>
    std::unordered_map<int, int> index_;  // value -> position
    std::mt19937 generator_;

public:
    RandomizedSet()
        : generator_(std::random_device{}()) {
    }

    bool insert(int val) {
        if (index_.contains(val)) {
            return false;
        }

        index_[val] = static_cast<int>(values_.size());
        values_.push_back(val);
        return true;
    }

    bool remove(int val) {
        auto it = index_.find(val);

        if (it == index_.end()) {
            return false;
        }

        int removeIndex = it->second;
        int lastValue = values_.back();

        // 用最后一个元素覆盖要删除的位置。
        values_[removeIndex] = lastValue;
        index_[lastValue] = removeIndex;

        values_.pop_back();
        index_.erase(it);

        return true;
    }

    int getRandom() {
        std::uniform_int_distribution<int> distribution(
            0,
            static_cast<int>(values_.size()) - 1
        );

        return values_[distribution(generator_)];
    }
};


#include <random>
#include <unordered_map>
#include <unordered_set>
#include <vector>

class RandomizedCollection {
private:
    std::vector<int> values_;

    // value -> all positions of this value in values_
    std::unordered_map<int, std::unordered_set<int>> indices_;

    std::mt19937 generator_{std::random_device{}()};

public:
    RandomizedCollection() = default;

    bool insert(int val) {
        bool notPresent = !indices_.contains(val);

        indices_[val].insert(static_cast<int>(values_.size()));
        values_.push_back(val);

        return notPresent;
    }

    bool remove(int val) {
        auto mapIt = indices_.find(val);

        if (mapIt == indices_.end()) {
            return false;
        }

        // 任意删除 val 的一个位置。
        int removeIndex = *mapIt->second.begin();
        int lastIndex = static_cast<int>(values_.size()) - 1;
        int lastValue = values_.back();

        // 先从 val 的位置集合中移除 removeIndex。
        mapIt->second.erase(removeIndex);

        if (removeIndex != lastIndex) {
            // 用最后一个元素覆盖被删除的位置。
            values_[removeIndex] = lastValue;

            // 更新 lastValue 的位置集合：
            // lastIndex 不再存在，lastValue 现在位于 removeIndex。
            indices_[lastValue].erase(lastIndex);
            indices_[lastValue].insert(removeIndex);
        }

        values_.pop_back();

        // val 已经没有任何实例了，删除整个 map entry。
        if (mapIt->second.empty()) {
            indices_.erase(mapIt);
        }

        return true;
    }

    int getRandom() {
        std::uniform_int_distribution<int> distribution(
            0,
            static_cast<int>(values_.size()) - 1
        );

        return values_[distribution(generator_)];
    }
};
