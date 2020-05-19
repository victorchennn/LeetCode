package Companies.Google;

public class StrobogrammaticNumberIII {

    char[][] pairs = {{'0','0'},{'1','1'},{'6','9'},{'8','8'},{'9','6'}};
    int count = 0;

    public int strobogrammaticInRange(String low, String high) {
        for (int i = low.length(); i <= high.length(); i++) {
            dfs(low, high, new char[i], 0, i-1);
        }
        return count;
    }

    private void dfs(String low, String high, char[] build, int left, int right) {
        if (left > right) {
            String s = new String(build);
            if ((s.length() == low.length() && s.compareTo(low) < 0) ||
                    (s.length() == high.length() && s.compareTo(high) > 0)) {
                return;
            }
            count++;
            return;
        }
        for (char[] p : pairs) {
            build[left] = p[0];
            build[right] = p[1];
            if (build.length != 1 && build[0] == '0') {
                continue;
            }
            if (left == right && p[0] != p[1]) {
                continue;
            }
            dfs(low, high, build, left+1, right-1);
        }
    }
}
