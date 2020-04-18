package Companies.Google;

public class RepeatedStrMatch {
    public int repeatedStringMatch(String A, String B) {
        StringBuilder sb = new StringBuilder(A);
        int re = 1;
        while (sb.length() < B.length()) {
            sb.append(A);
            re++;
        }
        if (sb.indexOf(B) >= 0) {
            return re;
        }
        if (sb.append(A).indexOf(B) >= 0) {  // abcd cdabcdab
            return re+1;
        }
        return -1;
    }
}
