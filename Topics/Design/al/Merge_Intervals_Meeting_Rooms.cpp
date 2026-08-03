class Solution { // Employee_Free_Time_Meeting_Schedule.cpp
public:
    vector<vector<int>> merge(vector<vector<int>>& intervals) {
        if (intervals.size() <= 1) {
            return intervals;
        }

        sort(intervals.begin(), intervals.end(),
            [](const vector<int>& a, const vector<int>& b) {
                return a[0] < b[0];
            });

        vector<vector<int>> result;
        result.push_back(intervals[0]);

        for (int i = 1; i < intervals.size(); i++) {
            if (intervals[i][0] > result.back()[1]) {
                result.push_back(intervals[i]);
            } else {
                result.back()[1] = max(result.back()[1], intervals[i][1]);
            }
        }

        return result;
    }
};

// find the minimum number of conference rooms required to schedule all the meetings without any conflicts.
class Solution {
public:
    int minMeetingRooms(vector<vector<int>>& intervals) {
        if (intervals.empty()) {
            return 0;
        }

        sort(intervals.begin(), intervals.end(),
             [](const vector<int>& a, const vector<int>& b) {
                 return a[0] < b[0];
             });

        priority_queue<int, vector<int>, greater<int>> minHeap;  // 当前所有会议室的结束时间 从小到大
        minHeap.push(intervals[0][1]);

        for (int i = 1; i < intervals.size(); ++i) {
            if (intervals[i][0] >= minHeap.top()) {
                minHeap.pop();
            }
            minHeap.push(intervals[i][1]);
        }

        return static_cast<int>(minHeap.size());
    }
};


class Solution {
public:
    vector<vector<vector<int>>> assignMeetingRooms(vector<vector<int>>& intervals) {
        vector<vector<vector<int>>> rooms;
        if (intervals.empty()) {
            return rooms;
        }

        sort(intervals.begin(), intervals.end(),
            [](const vector<int>& a, const vector<int>& b) {
                return a[0] < b[0];
            });

        // {endingTime, roomIndex}
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> minHeap;
        for (const auto& interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            if (!minHeap.empty() && start >= minHeap.top().first) {
                // 复用最早结束的房间
                auto [previousEnd, roomIndex] = minHeap.top();
                minHeap.pop();

                rooms[roomIndex].push_back(interval);
                minHeap.push({end, roomIndex});
            } else {
                // 没有空闲房间，创建新房间
                int roomIndex = rooms.size();

                rooms.push_back({interval});
                minHeap.push({end, roomIndex});
            }
        }

        return rooms;
    }
};
