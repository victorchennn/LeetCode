package Companies.Google;

public class StudentAttendanceRecord {

    private int M = 1000000007;

    public int checkRecord(int n) {
        long[] p = new long[n+1];  // Sequence ends with P
        long[] lorp = new long[n+1];  // Sequence ends with P or L
        p[0] = lorp[0] = 1;
        p[1] = 1;
        lorp[1] = 2;
        for (int i = 2; i <= n; i++) {
            p[i] = lorp[i-1];
            lorp[i] = (p[i-2] + p[i-1] + p[i])%M;
        }
        long re = lorp[n];
        for (int i = 0; i < n; i++) {
            long temp = (lorp[i]*lorp[n-i-1])%M;
            re = (re + temp)%M;
        }
        return (int)re;
    }
}
