// specialization?
template <typename T>
struct Printer {
    void print(const T& value) {
        std::cout << value << '\n';
    }
};

// 但bool想特殊处理：
template <>
struct Printer<bool> {
    void print(bool value) {
        std::cout << (value ? "true" : "false") << '\n';
    }
};

template <typename T>
void print(const T& value) {
    if constexpr (std::is_same_v<T, bool>) {
        std::cout << (value ? "true" : "false");
    } else {
        std::cout << value;
    }
}

Printer<int> a;   // primary template
Printer<bool> b;  // specialization

// SFINAE Substitution Failure Is Not An Error

// primary template, no definition for has_foo<?>
// typename = void 第二个参数是void
template <typename T, typename = void>
struct has_foo 
    : std::false_type {};

// specialization
// decltype(std::declval<T>().foo()): 对于类型T，表达式t.foo()合不合法？
template <typename T>
struct has_foo<T, std::void_t<decltype(std::declval<T>().foo())>>
    : std::true_type {};

struct A {
    void foo() {}
};

struct B {};

// (C++20) 1. 定义 concept
template <typename T>
concept HasFoo = requires(T t) { // 不会真的创建 t，也不会真的调用 foo()。
    t.foo(); // 编译器只是在 compile time 问： 表达式 t.foo() 合法吗？
};

// 2. 限制 template
template <HasFoo T> // Compile time禁止不符合要求的类型 -> T必须满足 HasFoo
void callFoo(T& obj) { // A a; process(a); OK but B b; process(b); Compile Error
    obj.foo();
}

// 3. 不限制，而是内部选择
template <typename T>
void process(T& obj) {
    if constexpr (requires { obj.foo(); }) { // if constexpr (HasFoo<T>)
        obj.foo();
    } else {
        std::cout << "no foo";
    }
}

template <typename T> 
T maxValue(const T& a, const T& b) {
    return a > b ? a : b;
}
// 我其实不关心int double or whatever T
template <typename T>
concept Comparable = requires(T a, T b) { // compile-time polymorphism
    a > b;
};

template <Comparable T>
T maxValue(const T& a, const T& b) { // templates + concepts
    return a > b ? a : b;
}


// To execute C++, please define "int main()"
int main() {
  
  // has_foo<A, void>
  // 有foo()/void_t<合法类型>/void/has_foo<T, void>/specialization匹配/true
  std::cout << has_foo<A>::value << std::endl;

  // ::value是false_type/true_type自带的 相当于自己写
  // template <typename T, typename = void>
  // struct has_foo {
  //     static constexpr bool value = false;
  // };

  // const → 不能修改 
  // constexpr → 可以用于 compile-time evaluation
  // consteval (C++20)→ 必须 compile-time evaluation

  // has_foo<B, void> 退回primary template
  // 没有foo()/decltype(...)失败/specialization被SFINAE排除/primary template/false
  std::cout << has_foo<B>::value << std::endl;

  static_assert(HasFoo<A>);  // 编译阶段就知道HasFoo<A> true
  static_assert(!HasFoo<B>);

  return 0;
}


class Singleton {
public:
    static Singleton& getInstance() {
        static Singleton instance;
        return instance;
    }

    Singleton(const Singleton&) = delete;
    Singleton& operator=(const Singleton&) = delete;

    Singleton(Singleton&&) = delete;
    Singleton& operator=(Singleton&&) = delete;

private:
    Singleton() = default;
};

// Singleton s;          // ❌ constructor private
// Singleton* s = new Singleton(); // ❌
// Singleton::getInstance(); // 第一次：创建 instance
// Singleton::getInstance(); // 返回之前那个
// Singleton::getInstance(); // 返回之前那个

// OrderService(ILogger& logger) dependency injection maybe is better
