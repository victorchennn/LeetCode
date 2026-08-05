// Given a string num that contains only digits and an integer target, 
// return all possibilities to insert the binary operators '+', '-', and/or '*' between the digits of num 
// so that the resultant expression evaluates to the target value.
// Input: num = "123", target = 6
// Output: ["1*2*3","1+2+3"]
// Explanation: Both "1*2*3" and "1+2+3" evaluate to 6.

class Solution {
public:
    vector<string> addOperators(string num, int target) {
        vector<string> result;
        dfs(num, target, 0, 0, 0, "", result);
        return result;
    }

private:
    void dfs(const string& num, long long target, int index,
             long long value, long long prev,
             string expr, vector<string>& result) {

        if (index == num.size()) {
            if (value == target) result.push_back(expr);
            return;
        }

        long long cur = 0;
        for (int i = index; i < num.size(); i++) {
            if (i > index && num[index] == '0') break;

            cur = cur * 10 + num[i] - '0';
            string part = num.substr(index, i - index + 1);

            if (index == 0)
                dfs(num, target, i + 1, cur, cur, part, result);
            else {
                dfs(num, target, i + 1, value + cur, cur, expr + "+" + part, result);
                dfs(num, target, i + 1, value - cur, -cur, expr + "-" + part, result);
                dfs(num, target, i + 1, value - prev + prev * cur, prev * cur, expr + "*" + part, result);
            }
        }
    }
};

