
class Solution {
public:
    int minTransfers(vector<vector<int>>& transactions) {
        unordered_map<int, int> balance;

        for (const auto& t : transactions) {
            int from = t[0];
            int to = t[1];
            int amount = t[2];

            balance[from] -= amount;
            balance[to] += amount;
        }

        vector<int> debts;
        for (const auto& [person, amount] : balance) {
            if (amount != 0) {
                debts.push_back(amount);
            }
        }

        return dfs(debts, 0);
    }

private:
    int dfs(vector<int>& debts, int start) {
        // 跳过已经结清的人
        while (start < debts.size() && debts[start] == 0) {
            ++start;
        }

        if (start == debts.size()) {
            return 0;
        }

        int result = INT_MAX;

        for (int i = start + 1; i < debts.size(); ++i) {
            // 必须一正一负，才能互相抵消
            if (debts[start] * debts[i] >= 0) {
                continue;
            }

            int original = debts[i];

            // 让 start 的余额全部交给 i
            debts[i] += debts[start];

            result = min(result, 1 + dfs(debts, start + 1));

            // 回溯
            debts[i] = original;

            // 如果两个人刚好完全抵消，这是最优配对，不必再试
            if (original + debts[start] == 0) {
                break;
            }
        }

        return result;
    }
};
