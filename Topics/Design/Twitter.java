package Topics.Design;

import java.util.*;

public class Twitter {
    private int timestamp = 0;
    private Map<Integer, User> map;

    /** Initialize your data structure here. */
    public Twitter() {
        map = new HashMap<>();
    }

    /** Compose a new tweet. */
    public void postTweet(int userId, int tweetId) {
        if (!map.containsKey(userId)) {
            User user = new User(userId);
            map.put(userId, user);
        }
        map.get(userId).post(tweetId);
    }

    /**
     * Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed
     * must be posted by users who the user followed or by the user herself. Tweets must be ordered
     * from most recent to least recent.
     */
    public List<Integer> getNewsFeed(int userId) {
        List<Integer> res = new ArrayList<>();
        if (!map.containsKey(userId)) {
            return res;
        }

        Set<Integer> users = map.get(userId).following;
        PriorityQueue<Tweet> pq = new PriorityQueue<>((a, b) -> b.time - a.time);
        for (int u : users) {
            Tweet tweet = map.get(u).head;
            if (tweet != null) {
                pq.offer(tweet);
            }
        }
        int k = 0;
        while (!pq.isEmpty() && k < 10) {
            Tweet tweet = pq.poll();
            res.add(tweet.id);
            k++;
            if (tweet.next != null) {
                pq.offer(tweet.next);
            }
        }
        return res;
    }

    /** Follower follows a followee. If the operation is invalid, it should be a no-op. */
    public void follow(int followerId, int followeeId) {
        if (!map.containsKey(followerId)) {
            User user = new User(followerId);
            map.put(followerId, user);
        }
        if (!map.containsKey(followeeId)) {
            User user = new User(followeeId);
            map.put(followeeId, user);
        }
        map.get(followerId).follow(followeeId);
    }

    /** Follower unfollows a followee. If the operation is invalid, it should be a no-op. */
    public void unfollow(int followerId, int followeeId) {
        if (!map.containsKey(followerId) || followerId == followeeId) {
            return;
        }
        map.get(followerId).unfollow(followeeId);
    }

    class Tweet {
        int id, time;
        Tweet next;

        Tweet(int id) {
            this.id = id;
            time = timestamp++;
        }
    }

    class User {
        int id;
        Set<Integer> following;
        Tweet head;

        User(int id) {
            this.id = id;
            following = new HashSet<>();
            follow(id); // include self
        }

        void follow(int id) {
            following.add(id);
        }

        void unfollow(int id) {
            following.remove(id);
        }

        void post(int id) {
            Tweet t = new Tweet(id);
            t.next = head;
            head = t;
        }
    }
}
