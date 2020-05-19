package Companies.Google;

public class BullsAndCows {
    public String getHint(String secret, String guess) {
        int[] marks = new int[10];
        int cow = 0, bull = 0;
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == guess.charAt(i)) {
                bull++;
                continue;
            }
            if (marks[secret.charAt(i)-'0'] > 0) {
                cow++;
            }
            if (marks[guess.charAt(i)-'0'] < 0) {
                cow++;
            }
            marks[secret.charAt(i)-'0']--;
            marks[guess.charAt(i)-'0']++;
        }
        return bull+"A"+cow+ "";
    }
}
