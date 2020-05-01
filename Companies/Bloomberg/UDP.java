package Companies.Bloomberg;

import java.util.*;

/**
 * https://www.geeksforgeeks.org/user-datagram-protocol-udp
 * At the Transport layer of the IP network stack, UDP (User Datagram Protocol) is the
 * preferred method for the delivery of live video streams. UDP offers reduced latency
 * over the reliability that TCP (Transmission Control Protocol) provides.
 *
 * When to use UDP?
 *  1. Reduce the requirement of computer resources.
 *  2. When using the Multicast or Broadcast to transfer.
 *  3. The transmission of Real-time packets, mainly in multimedia applications.
 *
 */
public class UDP {
    public void udp(List<Stream> l) {
        Map<Integer, Stream> m = new HashMap<>();
        int index = 1;
        for (Stream s : l) {
            m.put(s.index, s);
            while (m.containsKey(index)) {
                System.out.print(m.get(index).content + " ");
                m.remove(index);
                index++;
            }
            System.out.println();
        }
    }

    public void udpII(List<Stream> l) {
        TreeMap<Integer, String> m = new TreeMap<>();
        int index = 1;
        for (Stream s : l) {
            m.put(s.index, s.content); // O(logN) for insertion and lookup
            while (!m.isEmpty() && m.firstKey() == index) {
                System.out.print(m.pollFirstEntry().getValue() + " ");
                index++;
            }
            System.out.println();
        }
    }

    public static void main(String...args) {
        UDP test = new UDP();
        test.udp(Arrays.asList(new Stream(1, "a"),
                new Stream(2, "b"), new Stream(4, "d"),
                new Stream(5, "e"), new Stream(3, "c")));

        test.udp(Arrays.asList(new Stream(1, "a"),
                new Stream(2, "b"), new Stream(4, "d")));

        test.udpII(Arrays.asList(new Stream(1, "a"),
                new Stream(2, "b"), new Stream(4, "d"),
                new Stream(5, "e"), new Stream(3, "c")));

        test.udpII(Arrays.asList(new Stream(1, "a"),
                new Stream(2, "b"), new Stream(4, "d")));
    }

    static class Stream {
        int index;
        String content;

        Stream(int index, String content) {
            this.index = index;
            this.content = content;
        }
    }
}
