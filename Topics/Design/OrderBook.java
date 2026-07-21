import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class OrderBook {

    enum Side {
        BUY,
        SELL
    }

    static class Order {
        final long id;
        final Side side;
        final int price;
        int quantity;

        Order(long id, Side side, int price, int quantity) {
            this.id = id;
            this.side = side;
            this.price = price;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "id=" + id +
                    ", side=" + side +
                    ", price=" + price +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    static class OrderLocation {
        final Side side;
        final int price;

        OrderLocation(Side side, int price) {
            this.side = side;
            this.price = price;
        }
    }

    /*
     * Buy prices: highest first.
     *
     * At each price level, LinkedHashMap preserves insertion order,
     * so the first entry is the oldest order.
     */
    private final TreeMap<Integer, LinkedHashMap<Long, Order>> bids =
            new TreeMap<>((a, b) -> Integer.compare(b, a));

    /*
     * Sell prices: lowest first.
     */
    private final TreeMap<Integer, LinkedHashMap<Long, Order>> asks =
            new TreeMap<>();

    /*
     * orderId -> side and price
     *
     * Once we know the side and price, LinkedHashMap can remove the
     * order directly by ID in average O(1).
     */
    private final Map<Long, OrderLocation> orderIndex =
            new java.util.HashMap<>();

    public boolean addOrder(Order order) {
        if (order == null
                || order.id <= 0
                || order.price <= 0
                || order.quantity <= 0) {
            return false;
        }

        if (orderIndex.containsKey(order.id)) {
            return false;
        }

        if (order.side == Side.BUY) {
            matchBuyOrder(order);
        } else {
            matchSellOrder(order);
        }

        if (order.quantity > 0) {
            addToBook(order);
        }

        return true;
    }

    private void addToBook(Order order) {
        TreeMap<Integer, LinkedHashMap<Long, Order>> book =
                order.side == Side.BUY ? bids : asks;

        LinkedHashMap<Long, Order> priceLevel =
                book.computeIfAbsent(
                        order.price,
                        ignored -> new LinkedHashMap<>()
                );

        priceLevel.put(order.id, order);

        orderIndex.put(
                order.id,
                new OrderLocation(order.side, order.price)
        );
    }

    private void matchBuyOrder(Order incoming) {
        while (incoming.quantity > 0
                && !asks.isEmpty()
                && incoming.price >= asks.firstKey()) {

            Map.Entry<Integer, LinkedHashMap<Long, Order>> bestAsk =
                    asks.firstEntry();

            LinkedHashMap<Long, Order> priceLevel =
                    bestAsk.getValue();

            Map.Entry<Long, Order> restingEntry =
                    priceLevel.entrySet().iterator().next();

            Order restingOrder = restingEntry.getValue();

            int tradedQuantity =
                    Math.min(incoming.quantity, restingOrder.quantity);

            incoming.quantity -= tradedQuantity;
            restingOrder.quantity -= tradedQuantity;

            if (restingOrder.quantity == 0) {
                priceLevel.remove(restingOrder.id);
                orderIndex.remove(restingOrder.id);

                if (priceLevel.isEmpty()) {
                    asks.remove(bestAsk.getKey());
                }
            }
        }
    }

    private void matchSellOrder(Order incoming) {
        while (incoming.quantity > 0
                && !bids.isEmpty()
                && incoming.price <= bids.firstKey()) {

            Map.Entry<Integer, LinkedHashMap<Long, Order>> bestBid =
                    bids.firstEntry();

            LinkedHashMap<Long, Order> priceLevel =
                    bestBid.getValue();

            Map.Entry<Long, Order> restingEntry =
                    priceLevel.entrySet().iterator().next();

            Order restingOrder = restingEntry.getValue();

            int tradedQuantity =
                    Math.min(incoming.quantity, restingOrder.quantity);

            incoming.quantity -= tradedQuantity;
            restingOrder.quantity -= tradedQuantity;

            if (restingOrder.quantity == 0) {
                priceLevel.remove(restingOrder.id);
                orderIndex.remove(restingOrder.id);

                if (priceLevel.isEmpty()) {
                    bids.remove(bestBid.getKey());
                }
            }
        }
    }

    public boolean cancelOrder(long orderId) {
        OrderLocation location = orderIndex.get(orderId);

        if (location == null) {
            return false;
        }

        TreeMap<Integer, LinkedHashMap<Long, Order>> book =
                location.side == Side.BUY ? bids : asks;

        LinkedHashMap<Long, Order> priceLevel =
                book.get(location.price);

        if (priceLevel == null) {
            return false;
        }

        Order removedOrder = priceLevel.remove(orderId);

        if (removedOrder == null) {
            return false;
        }

        if (priceLevel.isEmpty()) {
            book.remove(location.price);
        }

        orderIndex.remove(orderId);

        return true;
    }

    /*
     * Simple cancel-and-replace implementation.
     * The modified order loses its original time priority.
     */
    public boolean modifyOrder(
            long orderId,
            int newPrice,
            int newQuantity
    ) {
        if (newPrice <= 0 || newQuantity <= 0) {
            return false;
        }

        OrderLocation location = orderIndex.get(orderId);

        if (location == null) {
            return false;
        }

        Side side = location.side;

        cancelOrder(orderId);

        return addOrder(
                new Order(
                        orderId,
                        side,
                        newPrice,
                        newQuantity
                )
        );
    }

    public Integer getBestBid() {
        return bids.isEmpty() ? null : bids.firstKey();
    }

    public Integer getBestAsk() {
        return asks.isEmpty() ? null : asks.firstKey();
    }

    public static void main(String[] args) {
        OrderBook book = new OrderBook();

        book.addOrder(new Order(1, Side.BUY, 100, 50));
        book.addOrder(new Order(2, Side.BUY, 100, 30));
        book.addOrder(new Order(3, Side.SELL, 102, 40));

        System.out.println("Best bid: " + book.getBestBid());
        System.out.println("Best ask: " + book.getBestAsk());

        /*
         * Incoming BUY 103 can match the resting SELL 102.
         */
        book.addOrder(new Order(4, Side.BUY, 103, 20));

        book.cancelOrder(2);

        /*
         * Cancel order 1 and re-add it at price 103.
         */
        book.modifyOrder(1, 103, 50);

        System.out.println("Best bid: " + book.getBestBid());
        System.out.println("Best ask: " + book.getBestAsk());
    }
}
