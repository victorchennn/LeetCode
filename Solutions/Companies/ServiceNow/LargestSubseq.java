package Companies.ServiceNow;

public class LargestSubseq {
    public int largest(int[] nums) {
        int l1 = 0, l2 = 0, l3 = 0, l4 = 0, l5 = 0, s1 = 0, s2 = 0, s3 = 0, s4 = 0;
        for (int i : nums) {
            if (i > 0) {
                if (i >= l1) {
                    l5 = l4;l4 = l3; l3 = l2; l2 = l1; l1 = i;
                } else if (i >= l2) {
                    l5 = l4;l4 = l3; l3 = l2; l2 = i;
                } else if (i >= l3) {
                    l5 = l4;l4 = l3; l3 = i;
                } else if (i >= l4) {
                    l5 = l4;l4 = i;
                } else if (i >= l5) {
                    l5 = i;
                }
            } else if (i < 0) {
                if (i <= s1) {
                    s4 = s3; s3 = s2; s2 = s1; s1 = i;
                } else if (i <= s2) {
                    s4 = s3; s3 = s2; s2 = i;
                } else if (i <= s3) {
                    s4 = s3; s3 = i;
                } else if (i <= s4) {
                    s4 = i;
                }
            }
        }
        return Math.max(l1*l2*l3*l4*l5, Math.max(l1*l2*l3*s1*s2, l1*s1*s2*s3*s4));
    }


}
