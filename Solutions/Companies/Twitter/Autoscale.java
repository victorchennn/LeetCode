package Companies.Twitter;

public class Autoscale {
    public int autoscale(int start, int[] util) {
        for (int i = 0; i < util.length; i++) {
            if (util[i] < 25 && start > 1) {
                start = (start+1)/2;
                i += 1;
            } else if (util[i] > 60 && start <= 100000000) {
                start *= 2;
                i += 10;
            }
        }
        return start;
    }

    public static void main(String...args) {
        Autoscale test = new Autoscale();
        System.out.println(test.autoscale(2, new int[]{25,23,1,2,3,4,5,6,7,8,9,10,76,80}));
    }
}
