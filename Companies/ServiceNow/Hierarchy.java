package Companies.ServiceNow;//   Input:
//    DD,Glynn,Dev,SF
//    Avi,Laks,VP,SF
//    Dave,Avi,Dev,Canada
//    Hari,DD,Dev,India
//    Jin,Glynn,Dev,SF
//    Ram,Avi,Dev,SF
//    Laks,Laks,CEO,SF
//    Glynn,Laks,Principal Engineer,SF
//    Laura,Laks,Senior Staff,Canada
//
//   Output:
//    Laks [CEO, SF]
//      Avi [VP, SF]
//          Dave [Dev, Canada]
//          Ram [Dev, SF]
//      Glynn [Principal Engineer, SF]
//          DD [Dev, SF]
//              Hari [Dev, India]
//          Jin [Dev, SF]
//      Laura [Senior Staff Engineer, Canada]
//    */

import java.util.*;

public class Hierarchy {
    public void dfs(List<String> l) {
        Map<String, List<String>> sub = new HashMap<>();
        Map<String, String> title = new HashMap<>();
        String root = null;
        for (String s : l) {
            String[] each = s.split(",");
            title.put(each[0], each[2] + ", " +each[3]);
            if (each[0].equals(each[1])) {
                root = each[0];
            } else {
                sub.computeIfAbsent(each[1], k -> new ArrayList<>()).add(each[0]);
            }
        }
        helper(title, sub, root, 0);
    }

    private void helper(Map<String, String> title, Map<String, List<String>> sub, String name, int layer) {
        String s = String.format("%"+(2*layer+1)+"s", "");
        System.out.println(s + name + " [" + title.get(name) + "]");
        if (sub.get(name) != null) {
            for (String subs : sub.get(name)) {
                helper(title, sub, subs, layer+1);
            }
        }
    }

    public static void main(String...args) {
        Hierarchy test = new Hierarchy();
        List<String> l = new ArrayList<>();
        l.add("DD,Glynn,Dev,SF");
        l.add("Avi,Laks,VP,SF");
        l.add("Dave,Avi,Dev,Canada");
        l.add("Hari,DD,Dev,India");
        l.add("Jin,Glynn,Dev,SF");
        l.add("Ram,Avi,Dev,SF");
        l.add("Laks,Laks,CEO,SF");
        l.add("Glynn,Laks,Principal Engineer,SF");
        l.add("Laura,Laks,Senior Staff,Canada");
        test.dfs(l);
    }
}
