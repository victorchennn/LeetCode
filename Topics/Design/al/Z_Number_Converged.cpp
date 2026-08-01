// 可以对 1 ... n 每个数字做 DFS / 模拟，并用一个全局状态数组记忆：

// 0：还没计算
// 1：当前搜索路径中
// 2：可以收敛到 1
// 3：不能收敛到 1

// 如果途中遇到：

// 1：收敛
// 已知收敛的数字：收敛
// 已知不收敛的数字：不收敛
// 当前路径已经出现过的数字：进入环，不收敛

// 不过计算过程中数字可能超过 n，所以用 unordered_map<long long, int> 保存状态。

class Solution {
public:
    vector<int> findConvergedNumbers(int n) {
        unordered_map<long long, int> state;
        state[1] = 2;  // 1 converges to 1

        vector<int> result;

        for (int x = 1; x <= n; ++x) {
            if (isConverged(x, state)) {
                result.push_back(x);
            }
        }

        return result;
    }

private:
    bool isConverged(long long start,
                     unordered_map<long long, int>& state) {
        vector<long long> path;
        long long current = start;

        while (true) {
            if (current == 1) {
                markPath(path, state, 2);
                return true;
            }

            if (state.count(current)) {
                if (state[current] == 2) {
                    markPath(path, state, 2);
                    return true;
                }

                if (state[current] == 3) {
                    markPath(path, state, 3);
                    return false;
                }

                // state[current] == 1:
                // current is already in the current path, so there is a cycle.
                markPath(path, state, 3);
                return false;
            }

            state[current] = 1;
            path.push_back(current);

            if (current % 2 == 0) {
                current /= 2;
            } else {
                // Prevent signed integer overflow.
                if (current > (LLONG_MAX + 1LL) / 3LL) {
                    markPath(path, state, 3);
                    return false;
                }

                current = current * 3 - 1;
            }
        }
    }

    void markPath(const vector<long long>& path,
                  unordered_map<long long, int>& state,
                  int finalState) {
        for (long long x : path) {
            state[x] = finalState;
        }
    }
};
