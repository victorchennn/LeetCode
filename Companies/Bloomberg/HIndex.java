package Companies.Bloomberg;

public class HIndex {
    public int hIndex(int[] citations) {
        int[] bucket = new int[citations.length+1];
        for (int c : citations) {
            if (c >= citations.length) {
                bucket[citations.length]++;
            } else {
                bucket[c]++;
            }
        }
        int count = 0;
        for (int i = citations.length; i >= 0; i--) {
            count += bucket[i];
            if (count >= i) {
                return i;
            }
        }
        return 0;
    }
}
