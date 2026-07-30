class Solution {
public:
    vector<vector<int>> employeeFreeTime(vector<vector<vector<int>>>& schedule) {
        vector<vector<int>> result;
        vector<vector<int>> timeline;

        // 收集所有区间
        for (const auto& employee : schedule) {
            for (const auto& interval : employee) {
                timeline.push_back(interval);
            }
        }

        if (timeline.empty()) {
            return result;
        }

        // 按开始时间排序
        sort(timeline.begin(), timeline.end(),
            [](const auto& a, const auto& b) {
                return a[0] < b[0];
            }
        );

        vector<int> prev = timeline[0];
        for (int i = 1; i < timeline.size(); ++i) {
            const auto& cur = timeline[i];
            if (prev[1] < cur[0]) {
                // 找到公共空闲时间
                result.push_back({prev[1], cur[0]});
                prev = cur;
            } else {
                // 合并区间
                prev[1] = max(prev[1], cur[1]);
            }
        }
        return result;
    }
};

// 每个员工内部必须有序；堆大小最多 K；如果改成至少 N 个员工空闲，要用扫描线而不是区间合并。
// 这和 Merge K Sorted Lists 完全一样?
