class Solution {
public:
    double stringToDouble(const string& s) {
        int i = 0;
        int n = s.size();

        // 1. 跳过前导空格
        while (i < n && isspace(static_cast<unsigned char>(s[i]))) {
            ++i;
        }

        // 2. 读取正负号
        int sign = 1;
        if (i < n && (s[i] == '+' || s[i] == '-')) {
            sign = (s[i] == '-') ? -1 : 1;
            ++i;
        }

        // 3. 读取整数部分
        double value = 0.0;
        bool hasDigit = false;

        while (i < n && isdigit(static_cast<unsigned char>(s[i]))) {
            int digit = s[i] - '0';
            value = value * 10.0 + digit;
            ++i;
            hasDigit = true;
        }

        // 4. 读取小数部分
        if (i < n && s[i] == '.') {
            ++i;

            double place = 0.1;

            while (i < n && isdigit(static_cast<unsigned char>(s[i]))) {
                int digit = s[i] - '0';
                value += digit * place;
                place *= 0.1;
                ++i;
                hasDigit = true;
            }
        }

        if (!hasDigit) {
            throw invalid_argument("Invalid number");
        }

        // 5. 读取科学计数法
        int exponent = 0;

        if (i < n && (s[i] == 'e' || s[i] == 'E')) {
            ++i;

            int exponentSign = 1;
            if (i < n && (s[i] == '+' || s[i] == '-')) {
                exponentSign = (s[i] == '-') ? -1 : 1;
                ++i;
            }

            bool hasExponentDigit = false;

            while (i < n && isdigit(static_cast<unsigned char>(s[i]))) {
                int digit = s[i] - '0';
                exponent = exponent * 10 + digit;
                ++i;
                hasExponentDigit = true;
            }

            if (!hasExponentDigit) {
                throw invalid_argument("Invalid exponent");
            }

            exponent *= exponentSign;
        }

        // 6. 跳过尾部空格
        while (i < n && isspace(static_cast<unsigned char>(s[i]))) {
            ++i;
        }

        // 还有其他字符，输入非法
        if (i != n) {
            throw invalid_argument("Invalid number");
        }

        return sign * value * powerOfTen(exponent);
    }

private:
    double powerOfTen(int exponent) {
        double result = 1.0;
        double base = 10.0;

        long long exp = exponent;

        if (exp < 0) {
            base = 0.1;
            exp = -exp;
        }

        // 快速幂
        while (exp > 0) {
            if (exp & 1) {
                result *= base;
            }

            base *= base;
            exp >>= 1;
        }

        return result;
    }
};
