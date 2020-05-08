package Companies.Amazon;

public class ReverseString {
    public String reverseStr(String s, int k) {
        char[] cs = s.toCharArray();
        int l = 0, r = s.length()-1;
        while (l < r) {
            int p = Math.min(l+k-1, r);
            reverse(cs, l, p);
            l += 2*k;
        }
        return new String(cs);
    }

    private void reverse(char[] s, int l, int r) {
        while (l < r) {
            char temp = s[r];
            s[r] = s[l];
            s[l] = temp;
            l++;
            r--;
        }
    }
}
