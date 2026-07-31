// 实际上不需要排列完 9 位。前 6 位确定后，直接计算结果，再检查结果是不是三位数且剩余数字刚好组成它，会更高效：
// 不能出现 0 && 结果中的数字不能重复 && 结果必须恰好使用剩余数字 9P6 = 60,480 而不是完整的：9! = 362,880
class Solution { 
public:
    int countEquations() {
        vector<int> digits(9);
        vector<bool> used(10, false);

        int count = 0;
        dfs(digits, used, 0, count);
        return count;
    }

private:
    void dfs(vector<int>& digits, vector<bool>& used, int index, int& count) {
        if (index == 9) {
            int abc = digits[0] * 100 + digits[1] * 10 + digits[2];
            int def = digits[3] * 100 + digits[4] * 10 + digits[5];
            int ghi = digits[6] * 100 + digits[7] * 10 + digits[8];
            if (abc + def == ghi) {
                ++count;
            }
            return;
        }

        for (int digit = 1; digit <= 9; ++digit) {
            if (used[digit]) {
                continue;
            }
            used[digit] = true;
            digits[index] = digit;
            dfs(digits, used, index + 1, count);
            used[digit] = false;
        }
    }
};

int main() {
    Solution solution;
    cout << solution.countEquations() << '\n';  // 336
}
