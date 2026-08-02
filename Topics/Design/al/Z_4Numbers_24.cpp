// 任意四个自然数，请判断他们是否能通过加减乘除获得24点。

class Solution {
public:
    bool canMake24(vector<int>& nums) {
        vector<double> numbers;
        for (int num : nums) {
            numbers.push_back(num);
        }
        return dfs(numbers);
    }

private:
    static constexpr double EPS = 1e-6;

    bool dfs(const vector<double>& numbers) {
        int n = numbers.size();
        if (n == 1) {
            return abs(numbers[0] - 24.0) < EPS;
        }

        // 每次选择两个不同的数字
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                vector<double> remaining;

                // 先放入没有被选择的数字
                for (int k = 0; k < n; ++k) {
                    if (k != i && k != j) {
                        remaining.push_back(numbers[k]);
                    }
                }

                double a = numbers[i];
                double b = numbers[j];

                vector<double> results = {a + b, a - b, b - a, a * b};
                if (abs(b) > EPS) {
                    results.push_back(a / b);
                }
                if (abs(a) > EPS) {
                    results.push_back(b / a);
                }

                for (double result : results) {
                    remaining.push_back(result);
                    if (dfs(remaining)) {
                        return true;
                    }
                    remaining.pop_back();
                }
            }
        }
        return false;
    }
};
