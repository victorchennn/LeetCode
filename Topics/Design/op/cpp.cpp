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
concept HasFoo = requires(T t) {
    t.foo();
};

// 2. 限制 template
template <HasFoo T>
void callFoo(T& obj) {
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

  static_assert(HasFoo<A>);
  static_assert(!HasFoo<B>);

  return 0;
}
