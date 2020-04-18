package Companies.Google;

import java.util.*;

public class CourseSchedule {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] nums = new int[numCourses];
        int[] re = new int[numCourses];
        Map<Integer, List<Integer>> m = new HashMap<>();
        for (int[] pre : prerequisites) {
            nums[pre[0]]++;
            m.computeIfAbsent(pre[1], k->new ArrayList<>()).add(pre[0]);
        }
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (nums[i] == 0) {
                q.add(i);
            }
        }
        int index = 0;
        while (!q.isEmpty()) {
            int cur = q.poll();
            re[index++] = cur;
            if (m.get(cur) == null) {
                continue;
            }
            for (int af : m.get(cur)) {
                nums[af]--;
                if (nums[af] == 0) {
                    q.add(af);
                }
            }
        }
        return index == numCourses? re : new int[0];
    }
}
