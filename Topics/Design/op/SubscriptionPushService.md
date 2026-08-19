Design an object-oriented publish/subscribe notification service for user–topic subscriptions.

Provide the following APIs:

* addSubscription(userId, topicId)
* unsubscribe(userId, topicId)
* publishNews(topicId, newsId, payload)
* onNewsReceived(userId, newsId) // client acknowledgement

```
Machine
└── SubscriptionService process
      ├── Thread 1
      ├── Thread 2
      ├── Thread 3
      └── Thread 4

                         ONE SERVICE INSTANCE / PROCESS

Clients
   |
   | TCP / WebSocket
   v
+---------------------------+
| I/O Threads               |
|                           |
| non-blocking sockets      |
| epoll                     |
|                           |
| 负责：                    |
| - accept                  |
| - recv                    |
| - send                    |
| - socket readiness        |
| - lightweight parsing     |
+---------------------------+
             |
             | Request / Command  tryPush() 
             v
          Work Queue
             | waitAndPop()
     +-------+-------+
     |       |       |
     v       v       v
  Worker1 Worker2 Worker3
     |       |       |
     | 负责：
     | - business logic
     | - DB access
     | - calculation
     | - state update
     |
     +-------+-------+
             |
             v
        Shared State
             |
          mutex / lock
```

那怎么避免 MPSC？你之前 trading 里学过的一个很好的办法就是：
不要让多个 producer 竞争同一条 queue；给每个 producer-owner pair 一条 SPSC。

```
                Clients / API threads
          subscribe / unsubscribe / publish
                       |
                       v
              route by topicId
                       |
        +--------------+--------------+
        |                             |
        v                             v
   AAPL Topic Owner              MSFT Topic Owner
   single writer                 single writer
        |                             |
        | assign offset               |
        | update subscription state   |
        v                             v
   AAPL SPMC Ring                MSFT SPMC Ring
 [100][101][102]               [500][501][502]
    ^      ^    ^                  ^       ^
    |      |    |                  |       |
 Alice   Bob Charlie             Alice    Tom
 readSeq readSeq readSeq         readSeq  readSeq
```

hash(AAPL) → Owner0 所有subscribe(AAPL) unsubscribe(AAPL) publish(AAPL) 都去 Owner0。

```
             上层 System Design

Client
↓
Network
↓
I/O Thread
↓
Queue
↓
Worker
↓
Shared State

             ↓ 优化

Owner / Partition
↓
Single Writer

             ↓ 再往底层

SPSC / MPSC
↓
atomic
↓
acquire/release
↓
cache line

```

```cpp
unordered_map<int, unordered_set<int>> topicSubscribers;
// AAPL -> Alice, Bob, Charlie
// MSFT -> Alice, David
for (user : topicSubscribers[AAPL]) {
    send(user, news);
}
```

为什么还需要反向 map
```cpp
unordered_map<UserId, unordered_set<TopicId>> userSubscriptions;
// Alice -> AAPL MSFT
// Bob   -> AAPL
unsubscribe(Alice, AAPL) 可以快速从两边删除。
```

unsubscribe 和 publish 同时发生? 
如果 news 已经 publish，但用户在真正 delivery 之前 unsubscribe，那么也不能发送 所以 send 前必须重新验证 subscription
```cpp
for (auto user : users) {
    if (!isStillSubscribed(user, topic))
        continue;
    send(user, news);
}
```

ordering -> per-topic per-user ordering (每个 topic 有 monotonically increasing sequence / offset)
per-user-topic queue
```cpp
struct Subscription { 
    UserId userId;
    TopicId topicId; // Alice / AAPL:
    bool active = true;
    deque<News> pending; // [news100, news101, news102]
};
```

Each published item is delivered at most once to users who are subscribed at the time of delivery.
```cpp
struct DeliveryState {
    uint64_t lastDeliveredOffset; sent: 100,101,102
    uint64_t lastAckedOffset; ack:100,101
};
```
At-most-once 和 retry 的矛盾: send 一次 失败就失败 不 retry

mutex能解决unsubscribe 和 publish 同时发生 send(user, news)通常是网络操作，可能很慢。你如果整个 publishNews() 都持有 mutex，we can snapshot
```cpp
void publishNews(TopicId topic, const News& news) {
    std::vector<UserId> users;
    {
        std::lock_guard<std::mutex> lock(mutex_);
        for (auto user : subscribers_[topic])
            users.push_back(user);
    } // lock released
    for (auto user : users) {
        send(user, news);
    }
}
```
use a per-subscription or per-topic lock?

两个线程同时 publishNews(AAPL) 怎么保证 ordering？atomic确实可以保证 offset 唯一

atomic 一个变量，不等于 atomic 整个 operation。queue 最后可能：[101, 100]

我需要把 assign sequence number + append/enqueue 变成一个有序的 logical operation。
```
                 publishNews(AAPL)
                         |
                         v
                +----------------+
                |   AAPL Topic   |
                | mutex          |
                | nextOffset=103 |
                +----------------+
                         |
                 assign offset 103
                         |
             find AAPL subscribers
                         |
              +----------+----------+
              |                     |
              v                     v
       Alice/AAPL queue        Bob/AAPL queue
       [101,102,103]           [102,103]
              |                     |
           Worker                 Worker
              |                     |
            Alice                   Bob
```
publishNews(AAPL, news100) 这条消息本质上不能被覆盖，因为 100、101、102 每条都可能需要处理。如果只有一个 publish thread 和一个 fanout thread： SPSC ring buffer

如果一个 event 要多个 consumer 看呢？I could use an SPMC-style ring buffer with a separate read sequence per consumer.
```
                    writeSeq
                       |
                       v

[100][101][102][103][   ][   ]

  ^       ^       ^
  |       |       |
fanout  logger  metrics
read1   read2    read3
```

Subscription 本身是 state，所以这里反而可以用 snapshot / version delivery thread 最关心：Alice 现在还订阅 AAPL 吗？
Seqlock 能不能用于 subscription state？\
Double buffering / snapshot publication 在这里怎么用

假设：Topic AAPL 当前 subscriber list snapshot A: [Alice, Bob, Charlie] reader/fanout thread 正在读 A

writer 有 subscribe/unsubscribe：David subscribe / Bob unsubscribe 不要直接改 A。而是在另一个 buffer：snapshot B:[Alice, Charlie, David]

```
            AAPL Ring

writer
  |
  v
[100][101][102][103]
  ^         ^
  |         |
Alice      Bob
readSeq    readSeq
```
write = publish AAPL news 的线程 read  = 每个 AAPL subscriber Alice 下一条读 100，Bob 下一条读 103。 
I would not let the slowest subscriber block the topic writer. 
If a subscriber falls behind the ring capacity, old entries can be overwritten and that subscriber detects the sequence gap and skips forward.

What metrics and alerts would prove the design is healthy after launch?
- 主要看四类：traffic、latency、ring health、correctness

Traffic / Throughput publish rate？delivery rate? active subscribers? delivery 才是真正的负载。

Latency publish → client receive p50 p95 p99 p99.9

SPMC ring 最值得监控的指标 consumer lag = writeSeq - readSeq? consumer_lag / max_consumer_lag / ring_utilization alert：
max consumer lag > 80% ring capacity

Dropped / overwritten events 因为我们这个设计允许 slow consumer 被覆盖，所以这个指标必须有。

Correctness metrics duplicate_delivery_count? out_of_order_delivery_count? 

ACK 也可以作为健康指标 onNewsReceived(userId, newsId) sent count / ack count

I would monitor per-core and per-topic-owner CPU utilization rather than only host-level average CPU, 
because a hot topic could saturate a single-writer core while the rest of the machine remains idle.




