#include <cstddef>
#include <list>
#include <unordered_map>
#include <utility>

std::vector<T, std::allocator<T>>



class LRUCache {
private:
    using Entry = std::pair<int, int>; // key, value
    using Iterator = std::list<Entry>::iterator;

    std::size_t capacity_;

    // front = most recently used
    // back  = least recently used
    std::list<Entry> items_;

    // key -> position in items_
    std::unordered_map<int, Iterator> cache_;

public:
    explicit LRUCache(int capacity)
        : capacity_(capacity > 0
                        ? static_cast<std::size_t>(capacity)
                        : 0) {}

    int get(int key) {
        auto it = cache_.find(key);
        if (it == cache_.end()) {
            return -1;
        }

        // Move the existing node to the front in O(1).
        items_.splice(items_.begin(), items_, it->second);
        return it->second->second;
    }

    void put(int key, int value) {
        if (capacity_ == 0) {
            return;
        }

        auto it = cache_.find(key);
        if (it != cache_.end()) {
            it->second->second = value;
            items_.splice(items_.begin(), items_, it->second);
            return;
        }

        items_.insert(items_.begin(), {key, value});
        cache_[key] = items_.begin();

        if (cache_.size() > capacity_) {
            const int leastRecentlyUsedKey = items_.back().first;

            cache_.erase(leastRecentlyUsedKey);
            items_.pop_back();
        }
    }
};
