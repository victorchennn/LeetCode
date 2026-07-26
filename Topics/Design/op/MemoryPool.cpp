// operator new
//         │
//         ▼
// void* memory_      ← 一整块没有类型的内存
//         │
// static_cast
//         ▼
// std::byte*         ← 可以按字节移动
//         │
// reinterpret_cast
//         ▼
// FreeNode*          ← 建立 free list
//         │
// placement new
//         ▼
// Order*             ← 真正存放对象

class MemoryPool {
private:
    struct FreeNode {
        FreeNode* next;
    };

    void* memory_;
    std::size_t blockSize_;
    std::size_t blockCount_;
    FreeNode* freeList_;

public:
    MemoryPool(std::size_t blockSize, std::size_t blockCount)
        : memory_(nullptr),
          blockSize_(
              blockSize < sizeof(FreeNode*)
                  ? sizeof(FreeNode*)
                  : blockSize
          ),
          blockCount_(blockCount),
          freeList_(nullptr) {

        if (blockCount_ == 0) {
            throw std::invalid_argument("blockCount must be positive");
        }

        memory_ = ::operator new(blockSize_ * blockCount_);

        auto* bytes = static_cast<std::byte*>(memory_);

        for (std::size_t i = 0; i < blockCount_; ++i) {
            auto* node = reinterpret_cast<FreeNode*>(bytes + i * blockSize_);
            node->next = freeList_;
            freeList_ = node;
        }
    }

    ~MemoryPool() {
        ::operator delete(memory_);
    }

    MemoryPool(const MemoryPool&) = delete;
    MemoryPool& operator=(const MemoryPool&) = delete;

    void* allocate() {
        if (freeList_ == nullptr) {
            return nullptr;
        }

        FreeNode* block = freeList_;
        freeList_ = freeList_->next;

        return block;
    }

    void deallocate(void* ptr) {
        if (ptr == nullptr) {
            return;
        }

        auto* block = static_cast<FreeNode*>(ptr);

        block->next = freeList_;
        freeList_ = block;
    }
};


struct Order {
    int id;
    double price;
    int quantity;
};

int main() {
    MemoryPool pool(sizeof(Order), 1000);

    void* memory = pool.allocate();
    if (memory == nullptr) {
        return 1;
    }

    Order* order = new (memory) Order{1, 100.5, 20};

    // 使用 order
    order->price = 101.0;

    // 显式调用析构函数
    order->~Order();

    // 把内存还给 pool
    pool.deallocate(order);
}
