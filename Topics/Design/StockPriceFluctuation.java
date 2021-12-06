package Topics.Design;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This implementation provides guaranteed log(n) time cost for the containsKey,
 * get, put and remove operations.
 */
public class StockPriceFluctuation {

    private Map<Integer, Integer> timeToPrice;
    private TreeMap<Integer, Integer> priceToTime;
    private int latestTimestamp;

    public StockPriceFluctuation() {
        timeToPrice = new HashMap<>();
        priceToTime = new TreeMap<>();
    }

    public void update(int timestamp, int price) {
        if (timeToPrice.containsKey(timestamp)) {
            int oldPrice = timeToPrice.get(timestamp);
            if (oldPrice == price) {
                return;
            }
            priceToTime.put(oldPrice, priceToTime.get(oldPrice)-1);
            if (priceToTime.get(oldPrice) == 0) {
                priceToTime.remove(oldPrice);
            }
        }
        timeToPrice.put(timestamp, price);
        priceToTime.put(price, priceToTime.getOrDefault(price, 0)+1);
        latestTimestamp = Math.max(latestTimestamp, timestamp);
    }

    public int current() {
        return timeToPrice.get(latestTimestamp);
    }

    public int maximum() {
        return priceToTime.lastKey();
    }

    public int minimum() {
        return priceToTime.firstKey();
    }
}
