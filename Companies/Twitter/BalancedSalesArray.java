package Companies.Twitter;

public class BalancedSalesArray {
    public int solution(int[] a) {
        int left = 0 ;
        int right = 0;
        for (int i : a) {
            right += i;
        }
        for (int i = 0; i < a.length; i++) {
            right -= a[i];
            if(left == right) {
                return i;
            }
            left += a[i];
        }
        long aa = left;
        return 0;
    }

    public static void main(String...args) {
        BalancedSalesArray test = new BalancedSalesArray();
        System.out.println(test.solution(new int[]{1,2,3,4,6}));
        System.out.println(test.solution(new int[]{1,2,3,3}));
    }
}
