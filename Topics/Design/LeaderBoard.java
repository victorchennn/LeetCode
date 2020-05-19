package Topics.Design;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 1.Use HashMap to record the people's score
 * 2.Use TreeMap to find the topK in O(KlogN) by traverse the treemap
 * 3.Reset we can just remove the key from the treemap which is O(logN), same for addScore().
 *
 * TreeMap: A Red-Black tree based NavigableMap implementation.
 * Guaranteed O(logN) time cost for the containsKey, get, put and remove operations.
 */
public class LeaderBoard {
    private Map<Integer, Integer> data; // id, score
    private TreeMap<Integer, Integer> board; // score, freq

    public LeaderBoard() {
        data = new HashMap<>();
        board = new TreeMap<>(Collections.reverseOrder());
    }

    /* O(logN) */
    public void addScore(int playerId, int score) {
        if (!data.containsKey(playerId)) {
            data.put(playerId, score);
            board.put(score, board.getOrDefault(score, 0)+1);
        } else {
            int oldScore = data.get(playerId);
            board.put(oldScore, board.get(oldScore)-1);
            if (board.get(oldScore) == 0) {
                board.remove(oldScore);
            }
            int newScore = oldScore + score;
            data.put(playerId, newScore);
            board.put(newScore, board.getOrDefault(newScore, 0)+1);
        }
    }

    /* O(KlogN) */
    public int top(int K) {
        int sum = 0;
        for (Map.Entry<Integer, Integer> top : board.entrySet()) {
            int score = top.getKey();
            int freq = top.getValue();
            int n = Math.min(freq, K);
            sum += score*n;
            K -= n;
            if (K == 0) {
                break;
            }
        }
        return sum;
    }

    /* O(logN) */
    public void reset(int playerId) {
        int oldScore = data.get(playerId);
        board.put(oldScore, board.get(oldScore)-1);
        if (board.get(oldScore) == 0) {
            board.remove(oldScore);
        }
        data.remove(playerId);
    }
}
