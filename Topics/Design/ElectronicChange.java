package Topics.Design;


/**
 * You work in an electronic exchange. Throughout the day, you receive ticks (trading data) which consists of
 * product name and its traded volume of stocks. Eg: {name: vodafone, volume: 20}
 * What data structure will you maintain if:
 *
 * You have to tell top k products traded by volume at end of day.
 * You have to tell top k products traded by volume throughout the day.
 * What's the most efficient solution that you can think of?
 *
 *
 * What you're looking for is a kind of map or dictionary which supports the following queries:
 *
 * @Add(key,x): add x to the total for that key, creating a new entry if it doesn't already exist.
 * @GetKLargest(k): return the keys/totals for the k largest entries.
 *
 * Let's say Q is the number of queries, and n is the number of distinct keys. We should assume that Q is much
 * larger than n; choosing the NYSE as an example, there are a few thousand stocks traded, and a few million trades per day.
 *
 * In the first scenario we assume that there are a large number of Add queries followed by one GetKLargest query.
 * Since the cost of the Add query dominates, we can use a hashtable so that Add takes O(1) time, and then at the
 * end of the day we can do GetKLargest in O(n log k) time using a priority queue of size k; note that we don't
 * need to sort the whole key-set in O(n log n) time just to find the k largest elements. The total cost of
 * answering Q queries is O(Q + n log k).
 *
 * In the second scenario, we assume there could be a large number of both kinds of query. The cost of either query
 * could dominate. A good option is to use an order statistic tree, which supports Add in O(log n) time, and
 * GetKLargest in O(k log n) time. To look up a company by name in the tree requires a separate index, which can
 * be maintained as a hashtable. The total cost is O(Qk log n) in the worst case.
 *
 * @see KthLargest PriorityQueue
 * @see LeaderBoard TreeMap
 *
 * @see StockTransaction
 */
public class ElectronicChange {
}
