// Given a string num that contains only digits and an integer target, 
// return all possibilities to insert the binary operators '+', '-', and/or '*' between the digits of num 
// so that the resultant expression evaluates to the target value.
// Input: num = "123", target = 6
// Output: ["1*2*3","1+2+3"]
// Explanation: Both "1*2*3" and "1+2+3" evaluate to 6.

class Solution {
private:
    vector<string> result;
    string num;
    long long target;

    void dfs(int index, long long value, long long previous, string expression) {
        if (index == num.size()) {
            if (value == target) {
                result.push_back(expression);
            }
            return;
        }

        long long current = 0;

        for (int end = index; end < num.size(); ++end) {
            if (end > index && num[index] == '0') {
                break;
            }

            current = current * 10 + (num[end] - '0');
            string part = num.substr(index, end - index + 1);

            if (index == 0) {
                dfs(end + 1, current, current, part);
            } else {
                dfs(end + 1, value + current, current, expression + "+" + part);
                dfs(end + 1, value - current, -current, expression + "-" + part);
                dfs(end + 1, value - previous + previous * current, previous * current, expression + "*" + part);
            }
        }
    }

public:
    vector<string> addOperators(string digits, int targetValue) {
        result.clear();
        num = std::move(digits);
        target = targetValue;

        dfs(0, 0, 0, "");
        return result;
    }
};
