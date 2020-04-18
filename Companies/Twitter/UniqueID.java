package Companies.Twitter;

import java.util.Arrays;

public class UniqueID {
    public int unique(int[] array) {
        Arrays.sort(array);
        int sum = 0, low = 0;
        for (int num : array) {
            low = Math.max(low+1, num);
            sum += low;
        }
        return sum;
    }

    public static void main(String...args) {
        UniqueID test = new UniqueID();
        System.out.println(test.unique(new int[]{3,2,1,2,7}));
        System.out.println(test.unique(new int[]{4,2,1,2}));
    }
}
