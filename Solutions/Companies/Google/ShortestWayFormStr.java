package Companies.Google;

public class ShortestWayFormStr {
    public int shortestWay(String source, String target) {
        int re = 0, i = 0;
        while (i < target.length()) {
            int p = scan(source, target, i);
            if (p == i) {
                return -1;
            }
            re++;
            i = p;
        }
        return re;
    }

    private int scan(String source, String target, int start) {
        for (int i = 0; i < source.length(); i++) {
            if (start < target.length() && source.charAt(i) == target.charAt(start)) {
                start++;
            }
        }
        return start;
    }
}
