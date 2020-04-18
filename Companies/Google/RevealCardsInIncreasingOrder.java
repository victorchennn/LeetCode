package Companies.Google;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class RevealCardsInIncreasingOrder {
    public int[] deckRevealedIncreasing(int[] deck) {
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < deck.length; i++) {
            q.add(i);
        }
        int[] re = new int[deck.length];
        Arrays.sort(deck);
        for (int c : deck) {
            re[q.poll()] = c;
            if (!q.isEmpty()) {
                q.add(q.poll());
            }
        }
        return re;
    }
}
