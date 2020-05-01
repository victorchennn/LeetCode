package Companies.Bloomberg;

public class CarCollisions {
    public static int collisions(int[] nums) {
        int countZero = 0, sum = 0;
        for (int num : nums) {
            if (num == 0) {
                countZero++;
            } else {
                sum += countZero;
            }
        }
        return sum;
    }

    public static void main(String...args) {
        System.out.println(collisions(new int[]{0,1}));
        System.out.println(collisions(new int[]{0,0,1}));
        System.out.println(collisions(new int[]{1,0,1,0}));
        System.out.println(collisions(new int[]{0,1,0,1,1,0}));
    }
}
