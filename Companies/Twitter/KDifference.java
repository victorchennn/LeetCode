package Companies.Twitter;

import java.util.Arrays;

public class KDifference {
    public int solution(int[] array, int d) {
        Arrays.sort(array);
        int i = 0, j = 1, count = 0;
        while(j < array.length) {
            int diff = array[j] - array[i];
            if (diff == d) {
                count++;
                j++;
            } else if (diff > d) {
                i++;
            } else if (diff < d) {
                j++;
            }
        }
        return count;
    }

    public static void main(String...args) {
        KDifference test = new KDifference();
        System.out.println(test.solution(new int[]{1,2,3,4},1));
        System.out.println(test.solution(new int[]{1,5,3},2));
    }
}
