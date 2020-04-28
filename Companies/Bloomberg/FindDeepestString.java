package Companies.Bloomberg;

public class FindDeepestString {
    public static String find(String s) {
        int counter = 0, max = 0, l = -1, r = 0;
        String re = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') {
                counter++;
                if (counter > max) {
                    max = counter;
                    l = i;
                }
            } else if (c == '}') {
                if (counter == max) {
                    re = s.substring(l+1, i);
                }
                counter--;
            }
        }
        if (l == -1) {
            return s;
        }
        return re;
    }

    public static void main(String...args) {
        System.out.println(find("123"));
        System.out.println(find("{123}"));
        System.out.println(find("1{2{3{4}}5}}6"));
        System.out.println(find("{{{444}}{}{}}"));
    }
}
