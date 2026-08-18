Clients are traders. They need to get the current stock price. We have a server that stores the latest prices for all stocks. Design the system.

你第一批应该问这些问题里的核心几个：

* trader 是主动 query，还是希望 price change 时 server 主动 push？
* 大概多少 traders？
* 大概多少 stocks？
* price update frequency 多高？
* trader 可以接受多旧的数据？比如 1ms、100ms、1s？
* 每个 trader 看所有股票，还是只 subscribe 一部分？
* 更关心 throughput 还是 latency？

Traders want continuously updated prices. They don't want to repeatedly query the server -> publish / subscribe (state vs event)?
```
Exchange / Market Data
          ↓
     Price Server
          ↓
   Latest Price State
          ↓
 Subscription Manager
      ↙    ↓    ↘
 Trader A Trader B Trader C
```
How would you design the server-side data structure and the communication between the price server and traders?

state problem不是event queue problem. -> map <InstrumentId, Price> latestPrices // price structure includes timestamp/bid/ask/last/...
如果 instrument id 比较紧凑，而且数量固定，比如 20,000 个，我甚至更倾向于 vector<Price> latestPrices 

然后 subscription 可以单独维护：AAPL → [Trader1, Trader7, Trader20] MSFT → [Trader2, Trader7] vector<std::vector<ClientId>> subscribers;

How do you handle a slow trader who cannot consume updates as fast as the server produces them? 
如果 trader 很慢，我不会给每个 trader 建一个无限增长的 event queue，因为 requirement 是 只关心最新价格，不要求每个中间 update。

How does the sender know which instruments have changed, without scanning all instruments every time?
latest state dirty flag / changed set？

这时候一个比较适合 single-writer, multiple-reader, read-mostly state 的方案就是 seqlock。 writer 不需要等 reader：
What is the downside of seqlock? What happens if the writer updates extremely frequently? reader 可能不断 retry，甚至 starvation 不太适合：
✗ writer 持续高频更新
✗ write operation 很长
✗ reader 必须保证在有限时间内完成
✗ reader retry 成本很高

这里要区分“全局 writer 很频繁”和“同一个 instrument 的 writer 频繁到 reader 几乎抢不到窗口”。 如果每个 instrument 都有独立的 PriceState 
那么读 AAPL 的 trader 只和 AAPL 的更新频率竞争，不是和全市场所有更新竞争。seqlock per instrument 仍然可能很合理

实在不行 double/triple buffering 或 snapshot publication 意思就是不要让 reader 和 writer 同时碰同一份数据
Double buffering 的思路是：那干脆 writer 写另一份，reader 继续读旧的。这和seqlock保持所有数据要一致不一样
但是单纯两个 buffer + atomic active index并不能自动保证多个并发 reader 的安全。 Triple Buffer？

What if thousands of traders all request AAPL at the same time? 如果 AAPL 极热，所有 core 都一直读，而 writer 又一直更新，
cache line 会在 writer core 和 reader cores 之间不断做 coherence，这时候可以考虑 replicated read copies

event-driven I/O？
```
                  Market Data Thread
                         ↓
                 Latest Price State
                         ↑
                         │ read
                         │
                  Client I/O Thread
                         ↑
                       epoll
                ↗        ↑        ↖
          Trader A    Trader B    Trader C
```
