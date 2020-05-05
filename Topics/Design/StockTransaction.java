package Topics.Design;

import java.util.*;

/**
 * @see ElectronicChange
 */
public class StockTransaction {
    private TreeMap<Integer, Stock> topK;
    private Map<String, Stock> dataset;

    public StockTransaction () {
        topK = new TreeMap<>(Collections.reverseOrder());
        dataset = new HashMap<>();
    }

    public void update(String stockName, int price, int volume, int time) {
        if (!dataset.containsKey(stockName)) {
            Stock newone = new Stock(stockName, price, volume);
            dataset.put(stockName, newone);
            topK.put(price, newone);
            newone.history.put(time, price);
        } else {
            Stock cur = dataset.get(stockName);
            cur.sumVolume += volume;
            cur.sum += price*volume;
            int curVWAP = cur.sum / cur.sumVolume;
            if (curVWAP > cur.maxVWAP) {
                cur.maxVWAP = curVWAP;
            }
            topK.put(curVWAP, cur);
            cur.history.put(time, price);
        }
    }

    /* TopK based on current VWAP */
    public List<String> topK(int k) {
        k = Math.min(k, topK.size());
        List<String> re = new LinkedList<>();
        for (Map.Entry<Integer, Stock> e : topK.entrySet()) {
            re.add(e.getValue().name);
            k--;
            if (k == 0) {
                break;
            }
        }
        return re;
    }

    /* VWAP(volume weighted average price) */
    public int getVWAP(String stockName) {
        if (!dataset.containsKey(stockName)) {
            return 0;
        }
        Stock cur = dataset.get(stockName);
        return cur.sum / cur.sumVolume;
    }

    /* Get current price of the stock */
    public int getPrice(String stockName, int timeStamp) {
        if (!dataset.containsKey(stockName)) {
            return 0;
        }
        Stock stock = dataset.get(stockName);
        if (stock.history.firstKey() > timeStamp) {
            return 0;
        }
        return stock.history.floorEntry(timeStamp).getValue();
    }

    /* Get the highest price of stock in time range */
    public int getMaxPriceInRange(String stockName, int timeStamp1, int timeStamp2) {
        if (!dataset.containsKey(stockName)) {
            return 0;
        }
        Stock stock = dataset.get(stockName);
        if (stock.history.firstKey() > timeStamp2 || stock.history.lastKey() < timeStamp1) {
            return 0;
        }
        int maxPrice = 0;
        NavigableMap<Integer, Integer> sub = dataset.get(stockName).history.subMap(timeStamp1, true, timeStamp2, true);
        for (Map.Entry<Integer, Integer> e : sub.entrySet()) {
            maxPrice = Math.max(maxPrice, e.getValue());
        }
        return maxPrice;
    }

    class Stock {
        String name;
        int price, volume;
        int maxVWAP, sumVolume, sum; // sum = sum_i^n(price*volume)
        TreeMap<Integer, Integer> history; // time, current price
        Stock (String name, int price, int volume) {
            this.name = name;
            this.price = price;
            this.volume = volume;

            this.sum = price*volume;
            this.sumVolume = volume;
            this.maxVWAP = price;

            history = new TreeMap<>();
        }
    }

//    class Transaction {
//        int timestamp, price, volume;
//        String name;
//        Transaction(int time, String name, int price, int volume) {
//            this.timestamp = time;
//            this.name = name;
//            this.price = price;
//            this.volume = volume;
//        }
//    }
}
