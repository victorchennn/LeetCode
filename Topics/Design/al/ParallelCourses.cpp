int minimumSemesters(int n, const std::vector<std::vector<int>>& relations) {
        std::vector<std::vector<int>> graph(n + 1);
        std::vector<int> indegree(n + 1, 0);

        for (const auto& relation : relations) {
            int prerequisite = relation[0];
            int course = relation[1];

            graph[prerequisite].push_back(course);
            ++indegree[course];
        }

        std::queue<int> queue;

        for (int course = 1; course <= n; ++course) {
            if (indegree[course] == 0) {
                queue.push(course);
            }
        }

        int semesters = 0;
        int studiedCount = 0;

        while (!queue.empty()) {
            int levelSize = static_cast<int>(queue.size());
            ++semesters;

            for (int i = 0; i < levelSize; ++i) {
                int course = queue.front();
                queue.pop();

                ++studiedCount;

                for (int nextCourse : graph[course]) {
                    --indegree[nextCourse];

                    if (indegree[nextCourse] == 0) {
                        queue.push(nextCourse);
                    }
                }
            }
        }

        return studiedCount == n ? semesters : -1;
    }

// Return the minimum number of months needed to complete all the courses.
    int minimumTime(int n, const std::vector<std::vector<int>>& relations, const std::vector<int>& time) {
        std::vector<std::vector<int>> prerequisites(n + 1);

        for (const auto& relation : relations) {
            int prerequisite = relation[0];
            int course = relation[1];

            prerequisites[course].push_back(prerequisite);
        }

        std::vector<int> memo(n + 1, -1);

        int result = 0;

        for (int course = 1; course <= n; ++course) {
            result = std::max(
                result,
                dp(course, time, prerequisites, memo)
            );
        }

        return result;
    }

    int dp(
        int course,
        const std::vector<int>& time,
        const std::vector<std::vector<int>>& prerequisites,
        std::vector<int>& memo
    ) {
        if (memo[course] != -1) {
            return memo[course];
        }

        int prerequisiteTime = 0;

        for (int prerequisite : prerequisites[course]) {
            prerequisiteTime = std::max(
                prerequisiteTime,
                dp(prerequisite, time, prerequisites, memo)
            );
        }

        memo[course] = prerequisiteTime + time[course - 1];
        return memo[course];
    }
};
