// 操作	                        调用	              count变化
// SharedPtr p2 = p1;	          Copy constructor	  +1
// p2 = p1;	                    Copy assignment	    +1（并先释放 p2 原来的）
// SharedPtr p2(std::move(p1));	Move constructor	  不变
// p2 = std::move(p1);	        Move assignment	    不变（并先释放 p2 原来的）

template<typename T>
class SharedPtr {
public:
    SharedPtr() noexcept
        : ptr_(nullptr), count_(nullptr) {}

    explicit SharedPtr(T* ptr)
        : ptr_(ptr) {
        if (ptr) {
            count_ = new size_t(1);
        } else {
            count_ = nullptr;
        }
    }

    ~SharedPtr() {
        release();
    }

    // Copy constructor
    // SharedPtr<int> p1(new int(5));
    // SharedPtr<int> p2 = p1;
    SharedPtr(const SharedPtr& other)
        : ptr_(other.ptr_),
          count_(other.count_) {
        if (count_) {
            ++(*count_);
        }
    }

    // Copy assignment
    // SharedPtr<int> p1(new int(5));
    // SharedPtr<int> p2(new int(10));
    // p2 = p1;
    SharedPtr& operator=(const SharedPtr& other) {
        if (this != &other) {
            release();

            ptr_ = other.ptr_;
            count_ = other.count_;

            if (count_) {
                ++(*count_);
            }
        }
        return *this;
    }

    // Move constructor
    // SharedPtr<int> p1(new int(5));
    // SharedPtr<int> p2(std::move(p1));
    SharedPtr(SharedPtr&& other) noexcept
        : ptr_(other.ptr_),
          count_(other.count_) {
        other.ptr_ = nullptr;
        other.count_ = nullptr;
    }

    // Move assignment
    // SharedPtr<int> p1(new int(5));
    // SharedPtr<int> p2(new int(10));
    // p2 = std::move(p1);
    SharedPtr& operator=(SharedPtr&& other) noexcept {
        if (this != &other) {
            release();

            ptr_ = other.ptr_;
            count_ = other.count_;

            other.ptr_ = nullptr;
            other.count_ = nullptr;
        }
        return *this;
    }

    T& operator*() const {
        return *ptr_;
    }

    T* operator->() const {
        return ptr_;
    }

    T* get() const {
        return ptr_;
    }

    size_t use_count() const {
        return count_ ? *count_ : 0;
    }

private:
    void release() {
        if (count_) {
            --(*count_);

            if (*count_ == 0) {
                delete ptr_;
                delete count_;
            }
        }

        ptr_ = nullptr;
        count_ = nullptr;
    }

private:
    T* ptr_;
    size_t* count_;
};

int main() {
    SharedPtr<int> p1(new int(5));
    {
        SharedPtr<int> p2 = p1;
        std::cout << p1.use_count() << std::endl; // 2
        {
            SharedPtr<int> p3 = p2;
            std::cout << p1.use_count() << std::endl; // 3
        }
        std::cout << p1.use_count() << std::endl; // 2
    }
    std::cout << p1.use_count() << std::endl; // 1
} // 最后 delete
