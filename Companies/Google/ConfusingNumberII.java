package Companies.Google;

public class ConfusingNumberII {

    char[][] dict = {{'0','0'},{'1','1'},{'6','9'},{'8','8'},{'9','6'}};

    public int confusingNumberII(int N) {
        String num = Integer.toString(N);
        int total = findTotal(num);
        for (int i = 1; i <= num.length(); i++) {
            total -= dfs(num, new char[i], 0, i-1);
        }
        return total;
    }

    private int dfs(String num, char[] c, int left, int right) {
        int re = 0;
        if (left > right) {
            String s = new String(c);
            if (s.length() < num.length() || s.compareTo(num) < 0) {
                re++;
                return re;
            }
        } else {
            for (char[] d : dict) {
                c[left] = d[0];
                c[right] = d[1];
                if ((c.length > 1 && c[0] == '0') || (left == right && d[0] != d[1])) {
                    continue;
                }
                re += dfs(num, c, left+1, right-1);
            }
        }
        return re;
    }

    private int findTotal(String num) {
        if (num.length() == 0) {         // be careful!!
            return 1;
        }
        char first = num.charAt(0);
        int total = count(first) * (int) Math.pow(5, num.length()-1);
        if (first == '0' || first == '1' || first == '6' || first == '8' || first == '9') {
            total += findTotal(num.substring(1));
        }
        return total;
    }

    private int count(char first) {
        int re = 0;
        for (char[] cc : dict) {
            if (cc[0] < first) {
                re++;
            }
        }
        return re;
    }
}
