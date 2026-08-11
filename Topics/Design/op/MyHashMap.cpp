class MyHashMap {
private:
    struct ListNode {
        int key;
        int val;
        ListNode* next;

        ListNode(int k, int v)
            : key(k), val(v), next(nullptr) {}
    };

    static constexpr int SIZE = 10000;
    std::vector<ListNode*> buckets;

    int hash(int key) const {
        return key % SIZE;
    }

    // struct Bucket {
    //     ListNode* head = nullptr;
    //     mutable std::mutex mutex; // 即使当前对象是 const，这个成员也允许被修改
    // };
    // std::vector<std::unique_ptr<Bucket>> buckets_; // std::mutex 是 non-copyable / non-movable, 所以用pointer or even std::array<Bucket, SIZE> buckets_;
    ...
    // Bucket& bucket = *buckets_[hash(key)];
    // std::lock_guard<std::mutex> lock(bucket.mutex);

    // Return the previous node of key.
    ListNode* find(ListNode* head, int key) {
        ListNode* prev = head;
        while (prev->next && prev->next->key != key) {
            prev = prev->next;
        }
        return prev;
    }

public:
    MyHashMap() : buckets(SIZE, nullptr) {}

    void put(int key, int value) {
        // std::unique_lock lock(bucket.mutex);
        int idx = hash(key);

        if (!buckets[idx]) {
            buckets[idx] = new ListNode(-1, -1);   // dummy head
        }

        ListNode* prev = find(buckets[idx], key);

        if (!prev->next) {
            prev->next = new ListNode(key, value);
        } else {
            prev->next->val = value;
        }
    }

    int get(int key) {
        // std::shared_lock lock(bucket.mutex);
        int idx = hash(key); 

        if (!buckets[idx]) {
            return -1;
        }

        ListNode* prev = find(buckets[idx], key);

        if (!prev->next) {
            return -1;
        }

        return prev->next->val;
    }

    void remove(int key) {
        // std::unique_lock lock(bucket.mutex);
        int idx = hash(key);

        if (!buckets[idx]) {
            return;
        }

        ListNode* prev = find(buckets[idx], key);

        if (!prev->next) {
            return;
        }

        ListNode* node = prev->next;
        prev->next = node->next;
        delete node;
    }

    ~MyHashMap() {
        for (auto head : buckets) {
            while (head) {
                ListNode* next = head->next;
                delete head;
                head = next;
            }
        }
    }
};


// concurrent hashmap
static constexpr size_t N = 16;
struct Shard {
    std::mutex mutex;
    std::unordered_map<int, int> map;
};
std::array<Shard, N> shards;


size_t i = std::hash<int>{}(key) % N;
Shard& shard = shards[i];

std::lock_guard<std::mutex> lock(shard.mutex);

// if read heavy?
struct Shard {
    mutable std::shared_mutex mutex;
    std::unordered_map<int, int> map;
};
std::shared_lock lock(shard.mutex); // read
std::unique_lock lock(shard.mutex); // write




template <typename K, typename V, size_t NumShards = 64, typename Hash = std::hash<K>>
class ConcurrentHashMap {
private:
    static_assert((NumShards & (NumShards - 1)) == 0,
                  "NumShards must be power of two");

    struct alignas(64) Shard {
        mutable std::shared_mutex mutex;
        std::unordered_map<K, V, Hash> map;
    };

    std::array<Shard, NumShards> shards_;
    Hash hasher_;
    std::atomic<size_t> size_{0};

    size_t shardIndex(const K& key) const {
        return hasher_(key) & (NumShards - 1);
    }

    Shard& getShard(const K& key) {
        return shards_[shardIndex(key)];
    }

    const Shard& getShard(const K& key) const {
        return shards_[shardIndex(key)];
    }

public:
    explicit ConcurrentHashMap(size_t expectedSize = 0) {
        if (expectedSize > 0) {
            const size_t perShard =
                (expectedSize + NumShards - 1) / NumShards;

            for (auto& shard : shards_) {
                shard.map.reserve(perShard);
            }
        }
    }

    // ---------- READ ----------
    std::optional<V> get(const K& key) const {
        const Shard& shard = getShard(key);

        std::shared_lock lock(shard.mutex);

        auto it = shard.map.find(key);

        if (it == shard.map.end())
            return std::nullopt;

        return it->second;  // return copy
    }

    // ---------- INSERT ----------
    bool insert(const K& key, const V& value) {
        Shard& shard = getShard(key);

        std::unique_lock lock(shard.mutex);

        auto [it, inserted] =
            shard.map.emplace(key, value);

        if (inserted)
            size_.fetch_add(1, std::memory_order_relaxed);

        return inserted;
    }

    // ---------- INSERT OR UPDATE ----------
    void insertOrUpdate(const K& key, const V& value) {
        Shard& shard = getShard(key);

        std::unique_lock lock(shard.mutex);

        auto [it, inserted] =
            shard.map.try_emplace(key, value);

        if (!inserted) {
            it->second = value;
        } else {
            size_.fetch_add(1, std::memory_order_relaxed);
        }
    }

    // ---------- ATOMIC READ-MODIFY-WRITE ----------
    template <typename Func>
    bool update(const K& key, Func func) {
        Shard& shard = getShard(key);

        std::unique_lock lock(shard.mutex);

        auto it = shard.map.find(key);

        if (it == shard.map.end())
            return false;

        func(it->second);
        return true;
    }

    // ---------- ERASE ----------
    bool erase(const K& key) {
        Shard& shard = getShard(key);

        std::unique_lock lock(shard.mutex);

        if (shard.map.erase(key)) {
            size_.fetch_sub(1, std::memory_order_relaxed);
            return true;
        }

        return false;
    }

    size_t size() const {
        return size_.load(std::memory_order_relaxed);
    }
};
