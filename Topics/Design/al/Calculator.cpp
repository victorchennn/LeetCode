class BasicCalculator {
public:
    int calculate(string s) {
        s.push_back('+');
        int index = 0;
        return helper(s, index);
    }

private:
    int helper(const string& s, int& index) {
        int sum = 0;
        int prev = 0;
        int num = 0;
        char sign = '+';

        while (index < s.size()) {
            char c = s[index++];

            if (c == ' ') {
                continue;
            }

            if (isdigit(c)) {
                num = num * 10 + (c - '0');
            } else if (c == '(') {
                num = helper(s, index);
            } else {
                if (sign == '+') {
                    sum += prev;
                    prev = num;
                } else {
                    sum += prev;
                    prev = -num;
                }

                // switch (sign) {
                //     case '+':
                //         sum += prev;
                //         prev = num;
                //         break;
                    
                //     case '-':
                //         sum += prev;
                //         prev = -num;
                //         break;
                    
                //     case '*':
                //         prev *= num;
                //         break;
                    
                //     case '/':
                //         prev /= num;
                //         break;
                // }

                if (c == ')') {
                    break;
                }

                sign = c;
                num = 0;
            }
        }

        return sum + prev;
    }
};

int main() {
    BasicCalculator calculator;

    assert(calculator.calculate("1 + 2") == 3);
    assert(calculator.calculate("1 - 2 + 3") == 2);
    assert(calculator.calculate("(1 + 2) - 3") == 0);
    assert(calculator.calculate("1 - (2 - 3)") == 2);
    assert(calculator.calculate("-(2 + 3)") == -5);
    assert(calculator.calculate("1 - (-2)") == 3);

    cout << "All tests passed\n";
}

// ^ 方法二：两个栈
// 维护：nums ops

class BasicCalculator {
public:
    long long calculate(const string& s) {
        stack<long long> nums;
        stack<char> ops;

        for (int i = 0; i < static_cast<int>(s.size()); ++i) {
            char c = s[i];
            if (c == ' ') {
                continue;
            }

            if (isdigit(static_cast<unsigned char>(c))) {
                long long num = 0;

                while (i < static_cast<int>(s.size()) &&
                       isdigit(static_cast<unsigned char>(s[i]))) {
                    num = num * 10 + (s[i] - '0');
                    ++i;
                }

                --i;
                nums.push(num);
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (!ops.empty() && ops.top() != '(') {
                    apply(nums, ops);
                }

                if (ops.empty()) {
                    throw invalid_argument("Unmatched ')'");
                }

                ops.pop();
            } else if (isOperator(c)) {
                while (!ops.empty() &&
                       ops.top() != '(' &&
                       shouldApplyBefore(ops.top(), c)) {
                    apply(nums, ops);
                }

                ops.push(c);
            } else {
                throw invalid_argument("Invalid character");
            }
        }

        while (!ops.empty()) {
            if (ops.top() == '(') {
                throw invalid_argument("Unmatched '('");
            }

            apply(nums, ops);
        }

        if (nums.size() != 1) {
            throw invalid_argument("Invalid expression");
        }

        return nums.top();
    }

private:
    bool isOperator(char c) {
        return c == '+' ||
               c == '-' ||
               c == '*' ||
               c == '/' ||
               c == '^';
    }

    int precedence(char op) {
        if (op == '+' || op == '-') {
            return 1;
        }

        if (op == '*' || op == '/') {
            return 2;
        }

        if (op == '^') {
            return 3;
        }

        return 0;
    }

    bool shouldApplyBefore(char top, char current) {
        if (precedence(top) > precedence(current)) {
            return true;
        }

        if (precedence(top) < precedence(current)) {
            return false;
        }

        // 相同优先级时：
        // + - * / 是左结合，要先计算
        // ^ 是右结合，不能先计算
        return current != '^';
    }

    void apply(stack<long long>& nums, stack<char>& ops) {
        if (nums.size() < 2 || ops.empty()) {
            throw invalid_argument("Invalid expression");
        }

        long long right = nums.top();
        nums.pop();

        long long left = nums.top();
        nums.pop();

        char op = ops.top();
        ops.pop();

        long long result;

        switch (op) {
            case '+':
                result = left + right;
                break;

            case '-':
                result = left - right;
                break;

            case '*':
                result = left * right;
                break;

            case '/':
                if (right == 0) {
                    throw invalid_argument("Division by zero");
                }
                result = left / right;
                break;

            case '^':
                if (right < 0) {
                    throw invalid_argument(
                        "Negative exponent is not supported"
                    );
                }

                result = fastPow(left, right);
                break;

            default:
                throw invalid_argument("Unknown operator");
        }

        nums.push(result);
    }

    long long fastPow(long long base, long long exponent) {
        long long result = 1;

        while (exponent > 0) {
            if (exponent & 1) {
                result *= base;
            }

            base *= base;
            exponent >>= 1;
        }

        return result;
    }
};

int main() {
    BasicCalculator calculator;

    assert(calculator.calculate("2^3") == 8);
    assert(calculator.calculate("2^3^2") == 512);
    assert(calculator.calculate("(2^3)^2") == 64);
    assert(calculator.calculate("2^(3^2)") == 512);
    assert(calculator.calculate("2 + 3 * 2^3") == 26);
    assert(calculator.calculate("(2 + 3)^2") == 25);
    assert(calculator.calculate("10 - 2^3") == 2);

    cout << "All tests passed\n";
}
