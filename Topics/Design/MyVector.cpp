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
          std::construct_at(
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



















