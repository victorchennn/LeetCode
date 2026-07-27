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

// Memory Pool = 一块连续内存 + 一个指向空闲块的单链表头

class MemoryPool {
private:
    struct FreeNode {
        FreeNode* next;
    };
    
    std::byte* memory_;
    FreeNode* head_;
    
    std::size_t blockSize_;
    std::size_t blockCount_;
   

public:
    MemoryPool(std::size_t blockSize, std::size_t blockCount)
        : memory_(nullptr),
          head_(nullptr),
          blockSize_(std::max(blockSize, sizeof(FreeNode))),
          blockCount_(blockCount) {
            
        memory_ = static_cast<std::byte*>(
            ::operator new(blockSize_ * blockCount_)
        );

        for (std::size_t i = 0; i < blockCount_; ++i) {
            auto* node = reinterpret_cast<FreeNode*>(
                memory_ + i * blockSize_
            );

            node->next = head_;
            head_ = node;
        }
    }

    ~MemoryPool() {
        ::operator delete(memory_);
    }

    MemoryPool(const MemoryPool&) = delete;
    MemoryPool& operator=(const MemoryPool&) = delete;

    void* allocate() {
        if (head_ == nullptr) {
            throw std::bad_alloc();
        }

        FreeNode* block = head_;
        head_ = head_->next;

        return block;
    }

    void deallocate(void* ptr) {
        if (ptr == nullptr) {
            return;
        }

        auto* block = static_cast<FreeNode*>(ptr);

        block->next = head_;
        head_ = block;
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
