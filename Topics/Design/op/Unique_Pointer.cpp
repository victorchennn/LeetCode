template <typename T>
class UniquePtr {
public:
    // Default constructor
    UniquePtr() noexcept : ptr_(nullptr) {}

    // Construct from raw pointer
    explicit UniquePtr(T* ptr) noexcept : ptr_(ptr) {}

    // Destructor
    ~UniquePtr() {
        delete ptr_;
    }

    // Copy is not allowed (=delete means can't be called)
    UniquePtr(const UniquePtr&) = delete; // copy constructor UniquePtr<int> p2 = p1;
    UniquePtr& operator=(const UniquePtr&) = delete; // copy assignment operator UniquePtr<int> a(new int(1)); UniquePtr<int> b(new int(2)); b = a;

    // Move constructor
    UniquePtr(UniquePtr&& other) noexcept
        : ptr_(other.ptr_) {
        other.ptr_ = nullptr;
    }

    // Move assignment
    UniquePtr& operator=(UniquePtr&& other) noexcept {
        if (this != &other) {
            delete ptr_; // 先释放自己原来的资源

            ptr_ = other.ptr_; // 接管资源
            other.ptr_ = nullptr;  // 清空对方
        }

        return *this;
    }

    // Access the managed object
    T& operator*() const {
        return *ptr_;
    }

    T* operator->() const noexcept {
        return ptr_;
    }

    T* get() const noexcept {
        return ptr_;
    }

    explicit operator bool() const noexcept {
        return ptr_ != nullptr;
    }

    // Give up ownership without deleting
    T* release() noexcept {
        T* oldPtr = ptr_;
        ptr_ = nullptr;
        return oldPtr;
    }

    // Delete the current object and manage a new pointer
    void reset(T* newPtr = nullptr) noexcept {
        if (ptr_ != newPtr) {
            delete ptr_;
            ptr_ = newPtr;
        }
    }

    void swap(UniquePtr& other) noexcept {
        std::swap(ptr_, other.ptr_);
    }

private:
    T* ptr_;
};

template <typename T, typename... Args>
UniquePtr<T> makeUnique(Args&&... args) {
    return UniquePtr<T>(
        new T(std::forward<Args>(args)...)
    );
}

class Person {
public:
    explicit Person(std::string name)
        : name_(std::move(name)) {}

    void print() const {
        std::cout << name_ << '\n';
    }

private:
    std::string name_;
};

int main() {
    UniquePtr<Person> p1 = makeUnique<Person>("Victor");
    p1->print();

    // UniquePtr<Person> p2 = p1; // 编译错误：不能复制
    UniquePtr<Person> p2 = std::move(p1);

    if (!p1) {
        std::cout << "p1 is empty\n";
    }

    p2->print();
    p2.reset(new Person("Alice"));
    p2->print();

    Person* raw = p2.release();
    raw->print();

    delete raw; // release 后需要调用者自己负责 delete

     // 创建
    UniquePtr<int> p1 = makeUnique<int>(5);
    std::cout << *p1 << std::endl;   // 5

    // UniquePtr<int> p2 = p1;       // ❌ 编译错误，不能 copy

    // move
    UniquePtr<int> p2 = std::move(p1);
    if (!p1) {
        std::cout << "p1 is empty\n";
    }

    std::cout << *p2 << std::endl;   // 5

    // reset
    p2.reset(new int(10));

    std::cout << *p2 << std::endl;   // 10

    // release
    int* raw = p2.release();
    std::cout << *raw << std::endl;  // 10

    delete raw;  // release 后需要自己 delete
}
