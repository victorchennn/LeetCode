package Companies.Twitter;

import java.util.ArrayList;
import java.util.List;

public class WeirdFaculty {
    public int exam(List<Integer> v) {
        int totalSum = 0;
        for(int score: v) {
            if (score == 0) totalSum -= 1;
            else totalSum += 1;
        }

        int currSum = 0;
        for(int i =0; i < v.size(); i++) {
            if (currSum > totalSum) return i;
            currSum += v.get(i) == 0 ? -1 : 1;
            totalSum -= v.get(i) == 0 ? -1 : 1;
        }
        return v.size();
    }

    public static void main(String...args) {
        WeirdFaculty test = new WeirdFaculty();
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(0);
        l.add(0);
        l.add(1);
//        l.add(0);
        System.out.println(test.exam(l));
    }

}
