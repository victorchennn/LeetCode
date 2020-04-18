package Companies.Google;

import java.util.HashSet;
import java.util.Set;

public class RobotRoomCleaner {
    int[][] dirs = {{0,-1},{-1,0},{0,1},{1,0}};

    public void cleanRoom(Robot robot) {
        helper(robot, 0, 0, new HashSet<>(), 0);
    }

    private void helper(Robot robot, int x, int y, Set<String> set, int arrow) {
        String key = x+":"+y;
        if (set.contains(key)) {
            return;
        }
        robot.clean();
        set.add(key);
        for(int i = 0; i < 4; i++) {
            if (robot.move()) {
                int xx = x + dirs[arrow][0];
                int yy = y + dirs[arrow][1];
                helper(robot, xx, yy, set, arrow);
                robot.turnLeft();
                robot.turnLeft();
                robot.move();
                robot.turnLeft();
                robot.turnLeft();
            }
            robot.turnRight();
            arrow = (arrow+1)%4;
        }
    }

    interface Robot {
        public boolean move();
        public void turnLeft();
        public void turnRight();
        // Clean the current cell.
        public void clean();
    }
}


