package Companies.Microsoft;

import java.util.Arrays;

public class LargestNumber {
    public String largestNumber(int[] nums) {
        String[] ss = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            ss[i] = String.valueOf(nums[i]);
        }
        Arrays.sort(ss, (o1, o2)->{
            String s1 = o1 + o2;
            String s2 = o2 + o1;
            return s2.compareTo(s1);
        });
        if (ss[0].equals("0")) {
            return "0";
        }
        StringBuilder re = new StringBuilder();
        for (String s : ss){
            re.append(s);
        }
        return re.toString();
    }
}
