package Companies.Google;

import java.util.*;

public class SequenceReconstruction {
    public boolean sequenceReconstruction(int[] org, List<List<Integer>> seqs) {
        Map<Integer, List<Integer>> seq = new HashMap<>();
        Map<Integer, Integer> count = new HashMap<>();
        for (List<Integer> l : seqs) {
            for (int i = 0; i < l.size(); i++) {
                seq.putIfAbsent(l.get(i), new ArrayList<>());
                count.putIfAbsent(l.get(i), 0);
                if (i > 0) {
                    seq.get(l.get(i-1)).add(l.get(i));
                    count.put(l.get(i), count.get(l.get(i))+1);
                }
            }
        }
        if (count.size() != org.length) {
            return false;
        }
        Queue<Integer> q = new LinkedList<>();
        for (Map.Entry<Integer, Integer> m : count.entrySet()) {
            if (m.getValue() == 0) {
                q.add(m.getKey());
            }
        }
        int index = 0;
        while (!q.isEmpty()) {
            if (q.size() > 1) {
                return false;
            }
            int cur = q.poll();
            if (org[index++] != cur) {
                return false;
            }
            for (int i : seq.get(cur)) {
                count.put(i, count.get(i)-1);
                if (count.get(i) == 0) {
                    q.add(i);
                }
            }
        }
        return index == org.length;
    }

    public static void main(String...args) {
//        List<Integer> l1 = new ArrayList<>(Arrays.asList(5,2,6,3));
//        List<Integer> l2 = new ArrayList<>(Arrays.asList(4,1,5,2));
//        List<List<Integer>> l = new ArrayList<>();
//        l.add(l1);
//        l.add(l2);
//        Companies.Google.SequenceReconstruction test = new Companies.Google.SequenceReconstruction();
//        test.sequenceReconstruction(new int[]{4,1,5,2,6,3}, l);

        System.out.println(("" + 0).equals("" + '0'));
        char t2 = '0' == '0'? '9': (char) ('0' - '0' - 1);
        System.out.println(t2 + "000");
    }
}
