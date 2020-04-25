package Companies.Bloomberg;

import java.util.Random;

public class Rand10UsingRand7 {
    public int rand10() {
        while (true) {
            int num = (rand7()-1)*7 + rand7()-1;
            if (num < 40) {
                return num%10+1;
            }
        }
    }

    private int rand7() {
        Random rand = new Random();
        return rand.nextInt(7)+1;
    }
}
