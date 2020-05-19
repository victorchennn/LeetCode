package Companies.Twitter;

import java.util.Arrays;

public class ParkingDilemma {
    public int solution(int[] cars, int k) {
        int num = cars.length;
        Arrays.sort(cars);
        int re = Integer.MAX_VALUE;
        for (int i = 0; i < cars.length - k + 1; i++) {
            re = Math.min(re, cars[i+k-1]-cars[i]+1);
        }
        return re;
    }

    public static void main(String...args) {
        ParkingDilemma test = new ParkingDilemma();
        System.out.println(test.solution(new int[]{2,10,8,17}, 3));
    }
}
