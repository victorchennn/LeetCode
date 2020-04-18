package Companies.Google;

public class RLEIterator {
    int[] num;
    int index, count;

    public RLEIterator(int[] A) {
        index = 0;
        count = 0;
        num = A;
    }

    public int next(int n) {
        while (index < num.length/2) {
            if (count + n <= num[index*2]) {
                count += n;
                break;
            } else {
                n = n - (num[index*2]-count);
                count = 0;
                index++;
            }
        }
        return index != num.length/2? num[2*index+1] : -1;
    }

    public static void main(String...args) {
        RLEIterator test = new RLEIterator(new int[]{3,8,0,9,2,5});
        test.next(2);
        test.next(1);
        test.next(1);
        test.next(2);
    }
}
