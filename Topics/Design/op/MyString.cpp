class String {
private:
    char* data_;
    std::size_t size_;

public:
    String() // 默认构造函数
        : data_(new char[1]{'\0'}), // 在堆上申请一个长度为 1 的 char 数组，并把唯一的元素初始化为字符串结束符 '\0' 
          size_(0) {}

    explicit String(const char* str) { // const char* 构造函数
        if (!str) {
            size_ = 0;
            data_ = new char[1]{'\0'};
            return;
        }

        size_ = std::strlen(str);
        data_ = new char[size_ + 1];

        std::memcpy(data_, str, size_ + 1); // 把 str 中的 size_ + 1 个字节复制到 data_。
        // strcpy? 会重新扫描一次找 '\0' 相当于又调用了一遍strlen()
        // 这种源和目标有重叠时才必须用 memmove
    }

    String(const String& other) // 拷贝构造函数
        : data_(new char[other.size_ + 1]),
          size_(other.size_) {
        std::memcpy(data_, other.data_, size_ + 1);
    }

    String(String&& other) noexcept // 移动构造函数
        : data_(other.data_),
          size_(other.size_) {
        other.data_ = nullptr;
        other.size_ = 0;
    }

    String& operator=(const String& other) {  // 拷贝赋值函数
        if (this == &other) {
            return *this;
        }

        // 先申请新内存，避免 new 失败后破坏原对象
        char* newData = new char[other.size_ + 1];
        std::memcpy(newData, other.data_, other.size_ + 1);

        delete[] data_;

        data_ = newData;
        size_ = other.size_;

        return *this;
    }

    // 移动赋值函数
    String& operator=(String&& other) noexcept {
        if (this == &other) {
            return *this;
        }

        // 先释放当前对象拥有的资源
        delete[] data_;

        // 接管 other 的资源
        data_ = other.data_;
        size_ = other.size_;

        // other 不能继续拥有这块内存
        other.data_ = nullptr;
        other.size_ = 0;

        return *this;
    }

    // 析构函数
    ~String() {
        delete[] data_;
    }

    const char* c_str() const {
        return data_ ? data_ : "";
    }

    std::size_t size() const {
        return size_;
    }

    char& operator[](std::size_t index) {
        return data_[index];
    }

    const char& operator[](std::size_t index) const {
        return data_[index];
    }
};

int main() {
    String s1("hello");         // 普通构造

    String s2(s1);              // 拷贝构造
    String s3 = s1;             // 也是拷贝构造

    String s4(std::move(s1));   // 移动构造

    String s5("world");
    s5 = s2;                    // 拷贝赋值

    String s6("C++");
    s6 = std::move(s2);         // 移动赋值

    std::cout << s3.c_str() << '\n';
    std::cout << s4.c_str() << '\n';
    std::cout << s5.c_str() << '\n';
    std::cout << s6.c_str() << '\n';
}
