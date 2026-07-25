template<typename T>
class Vector {
  private:
      T* data_;
      size_t size_;
      size_t capacity_;


  static T* allocate(std::size_t n) { // does not rely on other variable(size, cap, data), utility function, not belong to any object
    return static_cast<T*> (::operator new(sizeof(T) * n)); // without static_cast is void*, ::operator new is allocating memory
  }

  static void deallocate(T* ptr) noexcept { // delete is noexept itself
      ::operator delete(ptr); // delete only release memory, not destroy object
  }

  void destroy_elements() noexcept {
      std::destroy_n(data_, size_);
      // same as 
      for (size_t i = 0; i < size_; i++)
          std::destroy_at(data_ + i);
  }

  public:
    Vector() = default;

    ~Vector() {
        std::destroy_n(data_, size_); // destroy_elements() delete object 
        ::operator delete(data_); // deallocate(data_) free memory
    }

// copy constructor
Vector(const Vector& other) 
    : data_(allocate(other.capacity_)), // allocate use new memory
      size_(0),
      capacity_(other.capacity_) {

    try {
        for (; size_ < other.size_; ++size_) {
            std::construct_at(
                data_ + size_,
                other.data_[size_]
            );
        }
    } catch (...) {
        std::destroy_n(data_, size_);
        deallocate(data_);
        throw;
    }
}

// move constructor 
Vector(Vector&& other) noexcept // just assignment, should not throw error
    : data_(other.data_),
      size_(other.size_),
      capacity_(other.capacity_) {

    other.data_ = nullptr;
    other.size_ = 0;
    other.capacity_ = 0;
}

// copy assignment
Vector& operator=(const Vector& other) {
    if (this == &other) {
        return *this;
    }

    Vector temp(other);
    swap(temp); // after swap auto release old resource

    return *this;
}

void swap(Vector& other) noexcept {
    using std::swap;

    std::swap(data_, other.data_);
    std::swap(size_, other.size_);
    std::swap(capacity_, other.capacity_);
}

// move assignment 
Vector& operator=(Vector&& other) noexcept {
    if (this == &other) {
        return *this;
    }

    destroy_elements(); // free resource first, then same as move constructor
    deallocate(data_);

    data_ = other.data_;
    size_ = other.size_;
    capacity_ = other.capacity_;

    other.data_ = nullptr;
    other.size_ = 0;
    other.capacity_ = 0;

    return *this;
}

};

// reallocate?
{
  T* new_data = allocate(new_capacity);
  std::size_t constructed = 0;
  
  try { // construct, move old objects to new one
      for (; constructed < size_; ++constructed) {
          std::construct_at( // same as new (new_data + constructed) T(std::move(value));
              new_data + constructed,
              std::move_if_noexcept(data_[constructed]) // move is cheaper, but what if error and copying is available, it copies instead
             // This preserves the strong exception guarantee: if reallocation fails, the original vector remains unchanged
             // and we don't need to use old one anymore, so move better than copy
          );
      }
  } catch (...) {
      std::destroy_n(new_data, constructed);
      deallocate(new_data);
      throw;
  }
  
  std::destroy_n(data_, size_); // destroy old object
  deallocate(data_); // free old memory
  
  data_ = new_data; // update pointer
  capacity_ = new_capacity;
}

void reserve(std::size_t newCapacity) {
    if (newCapacity <= capacity_) {
        return;
    }

    reallocate(newCapacity);
}

void push_back(const T& value) {
    if (size_ == capacity_) {
        reserve(capacity_ == 0 ? 1 : capacity_ * 2);
    }

    std::construct_at(data_ + size_, value);
    ++size_;
}

void push_back(T&& value) {
    if (size_ == capacity_) {
        reserve(capacity_ == 0 ? 1 : capacity_ * 2);
    }

    std::construct_at(
        data_ + size_,
        std::move(value)
    );

    ++size_;
}

template<typename... Args>
T& emplace_back(Args&&... args) {

    if (size_ == capacity_) {
        reserve(capacity_ == 0 ? 1 : capacity_ * 2);
    }

    std::construct_at(
        data_ + size_,
        std::forward<Args>(args)...
    );

    return data_[size_++];
}

void pop_back() {
    if (empty()) {
        return;
    }

    --size_;
    std::destroy_at(data_ + size_);
}

void clear() noexcept {
    std::destroy_n(data_, size_);
    size_ = 0;
}

// insert, push everything behind 
for (size_t i = size_; i > index; --i) {
    std::construct_at(
        data_ + i,
        std::move_if_noexcept(data_[i-1])
    );

    std::destroy_at(data_ + i - 1);
}

std::construct_at(data_ + index, value);

++size_;


// 为什么 std::vector 不用 realloc()？
// 因为 realloc() 只是按字节搬内存，不会调用对象的移动构造或拷贝构造，也不会维护对象生命周期。对于拥有资源（如 std::string、std::vector、智能指针等）的类型，直接按字节复制会导致未定义行为。std::vector 必须逐个构造新对象、析构旧对象。
















