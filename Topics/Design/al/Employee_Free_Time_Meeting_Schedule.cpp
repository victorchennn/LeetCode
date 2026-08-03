class Solution { // Merge_Intervals_Meeting_Rooms.cpp
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

// schedule meeting
class Solution {
public:
    vector<int> minAvailableDuration(vector<vector<int>>& slots1, vector<vector<int>>& slots2, int duration) {
        // Sort both slot arrays by start time to process them in chronological order
        sort(slots1.begin(), slots1.end());
        sort(slots2.begin(), slots2.end());

        // Get the size of both slot arrays
        int slots1Size = slots1.size();
        int slots2Size = slots2.size();

        // Use two pointers to traverse both slot arrays
        int pointer1 = 0;
        int pointer2 = 0;

        // Find the earliest common available time slot
        while (pointer1 < slots1Size && pointer2 < slots2Size) {
            // Calculate the overlapping interval between current slots
            // The overlap starts at the later of the two start times
            int overlapStart = max(slots1[pointer1][0], slots2[pointer2][0]);
            // The overlap ends at the earlier of the two end times
            int overlapEnd = min(slots1[pointer1][1], slots2[pointer2][1]);

            // Check if the overlapping interval is long enough for the meeting
            if (overlapEnd - overlapStart >= duration) {
                // Return the earliest valid meeting time slot
                return {overlapStart, overlapStart + duration};
            }

            // Move the pointer pointing to the slot that ends earlier
            // This ensures we don't miss any potential overlaps
            if (slots1[pointer1][1] < slots2[pointer2][1]) {
                ++pointer1;
            } else {
                ++pointer2;
            }
        }

        // No valid common time slot found
        return {};
    }
};
