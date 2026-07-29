// you can take as many courses as you want, find the minimum number of semesters needed to complete all n courses
// T: O(n + m), S: O(n + m) n: number of courses / m: number of prerequisite relationships
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

        int semesters = 0, studiedCount = 0;
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
// V = number of courses / E = number of prerequisite relations
// Time:  O(V + E) Space: O(V + E)

// cycle? int processed = 0; every while loop processed++, check if (processed != n) in the end
// 
int minimumSemesters(int n, const std::vector<std::vector<int>>& relations, const std::vector<int>& time) {
        std::vector<std::vector<int>> graph(n + 1);
        std::vector<int> indegree(n + 1, 0);

        for (const auto& relation : relations) {
            int prerequisite = relation[0];
            int course = relation[1];

            graph[prerequisite].push_back(course);
            ++indegree[course];
        }

        std::queue<int> queue;
        vector<int> finish(n + 1, 0);
        for (int course = 1; course <= n; ++course) {
            if (indegree[course] == 0) {
                queue.push(course);
                finish[course] = time[course - 1];
            }
        }

        while (!queue.empty()) {
            int course = queue.front();
            queue.pop();
            for (int nextCourse : graph[course]) {
                finish[nextCourse] = max(finish[nextCourse], finish[course] + time[nextCourse - 1]);
                --indegree[nextCourse];
                if (indegree[nextCourse] == 0) {
                   queue.push(nextCourse);
                }
             }
        }
        return *std::max_element(finish.begin(), finish.end());
}

// Return the minimum number of months needed to complete all the courses.
// V = number of courses / E = number of prerequisite relations
// Time:  O(V + E) Space: O(V + E)
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
            result = std::max(result, dp(course, time, prerequisites, memo));
        }
        return result;
}

int dp(int course, const std::vector<int>& time, const std::vector<std::vector<int>>& prerequisites, std::vector<int>& memo) {
        if (memo[course] != -1) {
            return memo[course];
        }
        
        int prerequisiteTime = 0;
        for (int prerequisite : prerequisites[course]) {
            prerequisiteTime = std::max(prerequisiteTime, dp(prerequisite, time, prerequisites, memo));
        }
        
        memo[course] = prerequisiteTime + time[course - 1];
        return memo[course];
}


// Each semester, you can take at most k courses
int minNumberOfSemesters(int n, vector<vector<int>>& relations, int k) {
        vector<int> prerequisites(n + 1);
        for (auto& relation : relations) {
            prerequisites[relation[1]] |= 1 << relation[0];
        }
      
        queue<pair<int, int>> bfsQueue; // {completed courses bitmask, number of semesters}
        bfsQueue.push({0, 0}); 
      
        unordered_set<int> visited{{0}};
      
        while (!bfsQueue.empty()) {
            auto [completedCourses, semesters] = bfsQueue.front();
            bfsQueue.pop();
          
            // Check if all courses are completed (bits 1 to n are set)
            // (1 << (n + 1)) - 2 gives us a bitmask with bits 1 to n set
            if (completedCourses == (1 << (n + 1)) - 2) {
                return semesters;
            }
          
            // Find all courses that can be taken this semester
            // A course can be taken if all its prerequisites are completed
            int availableCourses = 0;
            for (int course = 1; course <= n; ++course) {
                // Check if all prerequisites for this course are completed
                if ((completedCourses & prerequisites[course]) == prerequisites[course]) {
                    availableCourses |= 1 << course;
                }
            }
          
            // Remove already completed courses from available courses
            availableCourses ^= completedCourses;
          
            // If we can take all available courses within the limit k
            if (__builtin_popcount(availableCourses) <= k) {
                int nextState = availableCourses | completedCourses;
                if (!visited.count(nextState)) {
                    visited.insert(nextState);
                    bfsQueue.push({nextState, semesters + 1});
                }
            } else {
                // We need to choose k courses from available courses
                // Enumerate all subsets of size k
                int subset = availableCourses;
                while (subset) {
                    // Only consider subsets with exactly k courses
                    if (__builtin_popcount(subset) == k) {
                        int nextState = subset | completedCourses;
                        if (!visited.count(nextState)) {
                            visited.insert(nextState);
                            bfsQueue.push({nextState, semesters + 1});
                        }
                    }
                    // Generate next subset using bit manipulation
                    // This iterates through all subsets of availableCourses
                    subset = (subset - 1) & availableCourses;
                }
            }
        }
      
        // Should not reach here if input is valid
        return 0;
    }
