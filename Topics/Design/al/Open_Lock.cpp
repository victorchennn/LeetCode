// deadends = ["0201","0101","0102","1212","2002"], target = "0202"
class Solution {
public:
    int openLock(vector<string>& deadends, string target) {
        unordered_set<string> dead(deadends.begin(), deadends.end());
        if (dead.contains("0000") || dead.contains(target)) {
            return -1;
        }
        if (target == "0000") {
            return 0;
        }

        unordered_set<string> left{"0000"};
        unordered_set<string> right{target};
        unordered_set<string> visited{"0000", target};

        int steps = 0;
        while (!left.empty() && !right.empty()) {
            // 始终扩展较小的一侧
            if (left.size() > right.size()) {
                swap(left, right);
            }

            unordered_set<string> next;

            for (const string& current : left) {
                for (int i = 0; i < 4; ++i) {
                    for (int direction : {-1, 1}) {
                        string neighbor = current;

                        int digit = neighbor[i] - '0';
                        digit = (digit + direction + 10) % 10;
                        neighbor[i] = static_cast<char>('0' + digit);

                        if (right.contains(neighbor)) {
                            return steps + 1;
                        }

                        if (dead.contains(neighbor) || visited.contains(neighbor)) {
                            continue;
                        }

                        visited.insert(neighbor);
                        next.insert(neighbor);
                    }
                }
            }

            left = std::move(next);
            ++steps;
        }

        return -1;
    }
};
