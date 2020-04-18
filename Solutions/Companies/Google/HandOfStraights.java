package Companies.Google;

public class HandOfStraights {
    public boolean isNStraightHand(int[] hand, int W) {
        int[] nums = new int[W];
        for (int i : hand) {
            nums[i%W]++;
        }
        int count = hand.length/W;
        for (int i : nums) {
            if (i != count) {
                return false;
            }
        }
        return true;

//        PriorityQueue<Integer> q = new PriorityQueue<>();
//        for (int i : hand) {
//            q.add(i);
//        }
//        while (!q.isEmpty()) {
//            int cur = q.poll();
//            for (int i = 1; i < W; i++) {
//                if (!q.isEmpty() && q.remove(cur+i)) {
//                    continue;
//                } else {
//                    return false;
//                }
//            }
//        }
//        return true;
    }
}
