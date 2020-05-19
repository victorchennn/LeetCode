package Companies.Microsoft;

public class CompareVersionNumbers {
    public int compareVersion(String version1, String version2) {
        String[] s1 = version1.split("\\.");
        String[] s2 = version2.split("\\.");
        int len = Math.max(s1.length, s2.length);
        for (int i = 0; i < len; i++) {
            Integer i1 = i < s1.length? Integer.valueOf(s1[i]):0;
            Integer i2 = i < s2.length? Integer.valueOf(s2[i]):0;
            int cp = i1.compareTo(i2);
            if (cp != 0) {
                return cp;
            }
        }
        return 0;
    }
}
