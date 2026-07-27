class Solution {
public:
    double myPow(double x, int n) {
        long long N = n;

        if (N < 0) {
            N = -N;
            x = 1.0 / x;
        }

        double result = 1.0;
        double product = x;

        while (N > 0) {
            if (N % 2 == 1) {
                result *= product;
            }

            product *= product;
            N /= 2;
        }

        return result;
    }
};

class Solution {
public:
    double myPow(double x, int n) {
        long long N = n;

        if (N < 0) {
            x = 1.0 / x;
            N = -N;
        }

        return helper(x, N);
    }

private:
    double helper(double x, long long n) {
        if (n == 0) {
            return 1.0;
        }

        double half = helper(x, n / 2);

        if (n % 2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }
};

bool almostEqual(double a, double b, double eps = 1e-9) {
    return std::abs(a - b) < eps;
}

int main() {
    Solution solution;

    // 零指数
    assert(almostEqual(solution.myPow(2.0, 0), 1.0));

    // 正指数
    assert(almostEqual(solution.myPow(2.0, 10), 1024.0));
    assert(almostEqual(solution.myPow(3.0, 3), 27.0));

    // 负指数
    assert(almostEqual(solution.myPow(2.0, -2), 0.25));
    assert(almostEqual(solution.myPow(4.0, -1), 0.25));

    // 负底数：奇偶指数
    assert(almostEqual(solution.myPow(-2.0, 3), -8.0));
    assert(almostEqual(solution.myPow(-2.0, 4), 16.0));
    assert(almostEqual(solution.myPow(-2.0, -3), -0.125));

    // 特殊底数
    assert(almostEqual(solution.myPow(1.0, INT_MIN), 1.0));
    assert(almostEqual(solution.myPow(-1.0, INT_MIN), 1.0));
    assert(almostEqual(solution.myPow(-1.0, INT_MAX), -1.0));

    // 小数底数
    assert(almostEqual(solution.myPow(2.1, 3), 9.261));

    // 零底数，正指数
    assert(almostEqual(solution.myPow(0.0, 5), 0.0));

    return 0;
}
