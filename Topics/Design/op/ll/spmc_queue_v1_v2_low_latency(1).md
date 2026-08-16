# SPMC Queue V1 / V2：Low-Latency Market Data Fan-out

## Slides

### SPMC queue overview

![SPMC queue overview](spmc_overview.png)

### SPMC V1 structure

![SPMC V1 structure](spmc_v1_structure.png)

### SPMC V1 Push

![SPMC V1 Push](spmc_v1_push.png)

### SPMC V1 Pop

![SPMC V1 Pop](spmc_v1_pop.png)

### SPMC V2 idea

![SPMC V2 idea](spmc_v2_idea.png)

### SPMC V2 per-block versions

![SPMC V2 per-block versions](spmc_v2_versions.png)

### SPMC V2 structure

![SPMC V2 structure](spmc_v2_structure.png)

### SPMC V2 Write

![SPMC V2 Write](spmc_v2_write.png)

这部分讨论的是 **Single Producer, Multiple Consumers (SPMC)** 的
market-data broadcast
queue。目标不是做一个普通"多个消费者竞争取走任务"的 queue，而是：

``` text
                    Strategy A
                       ↑
Market Data ──→ SPMC Queue ──→ Strategy B
                       ↓
                    Strategy C
```

每个 Consumer 都希望看到同一条 event stream。Low-latency
场景下尤其关心：

-   Producer 不能因为某个慢 Consumer 被阻塞。
-   尽量减少共享 cache line 和 contention。
-   Consumer 必须能发现自己已经落后太多、数据被覆盖（overflow）。
-   Event 可以是不同类型、不同大小，因此底层可以直接传 bytes。
-   Ring Buffer 预分配内存，hot path 尽量不做 allocation。

------------------------------------------------------------------------

# 1. 为什么普通 SPSC 不能直接扩展？

SPSC 最简单：

``` text
Producer:
    writeIndex

Consumer:
    readIndex
```

Producer 可以通过：

``` cpp
writeIndex - readIndex
```

判断 queue 是否 full。

最直接的 SPMC broadcast 扩展是：

``` text
Producer:
    writeIndex

Consumer A:
    readIndexA

Consumer B:
    readIndexB

Consumer C:
    readIndexC
```

Producer 为了避免覆盖任何 Consumer 尚未读取的数据，需要知道：

``` cpp
minReadIndex = min(readIndexA, readIndexB, readIndexC, ...);
```

问题是 Producer hot path 会不断读取所有 Consumer 的共享 counter：

``` text
Producer
 ├── readIndexA
 ├── readIndexB
 ├── readIndexC
 └── ...
```

Consumer 又不断修改自己的 read index，导致 cache-coherence traffic。

Low-latency broadcast 的另一个设计选择是：

> **Producer 不追踪 Consumer progress，也不接受 Consumer
> backpressure。慢 Consumer 自己检测 overflow 并 recovery。**

注意：

> "不维护 per-reader read index"不是说 Consumer 自己不知道读到哪里，而是
> **Producer/Queue 不读取每个 Consumer 的 progress**。

每个 Consumer 仍然可以本地保存：

``` cpp
uint64_t lastIndex;
```

但 Producer 完全不看它。

------------------------------------------------------------------------

# 2. SPMC V1：Global Publish/Pending Index

V1 的 Queue Header：

``` cpp
struct Q
{
    alignas(64) std::atomic<uint64_t> mIndex;
    alignas(64) std::atomic<uint64_t> mPendingIndex;
    alignas(64) uint8_t mData[0];
};
```

这里两个 index 都是 **Producer-side state**，不是
`writeIndex/readIndex`：

``` text
mIndex
    = 已经完整写完并 publish 到哪里

mPendingIndex
    = Producer 已经 reserve / 正在写到哪里
```

所以：

``` text
mIndex == mPendingIndex
    → 当前没有未完成 write

mIndex != mPendingIndex
    → Producer 正在写一段尚未 publish 的数据
```

Consumer 自己另外保存：

``` cpp
uint64_t lastIndex;
```

表示自己读到哪里。

------------------------------------------------------------------------

# 3. V1 Push：Reserve → Write → Publish

简化代码：

``` cpp
template <class WriteCallback>
void Q::Push(MessageSize size, WriteCallback writeCallback)
{
    // 1. Reserve / announce pending region
    mPendingIndex.fetch_add(size, std::memory_order_relaxed);

    // 2. Write record metadata + payload
    std::memcpy(mCurrent, &size, sizeof(MessageSize));

    writeCallback(
        mCurrent + sizeof(MessageSize)
    );

    // 3. Publish only after the complete record is ready
    mIndex.fetch_add(size, std::memory_order_release);

    // 4. Advance mCurrent and handle ring wrapping
}
```

概念流程：

``` text
mIndex = 1000
mPendingIndex = 1000

new record = 24 bytes

        ↓ reserve

mIndex = 1000
mPendingIndex = 1024

        ↓ write size
        ↓ write payload

mIndex = 1000
mPendingIndex = 1024

        ↓ publish

mIndex = 1024
mPendingIndex = 1024
```

关键原则：

> **先写完整 data，再推进 `mIndex`。**

否则 Consumer 可能看到新的 `mIndex` 后立刻读取，但 Producer
还只写了一半。

------------------------------------------------------------------------

# 4. 为什么使用 callback 写 bytes？

Queue 不一定只存一种类型：

``` cpp
struct Trade { ... };
struct AddOrder { ... };
struct CancelOrder { ... };
struct BookUpdate { ... };
```

因此 API 不一定设计成：

``` cpp
Push(const T& event);
```

而可以是：

``` cpp
Push(size, writeCallback);
```

例如：

``` cpp
Trade trade{...};

queue.Push(sizeof(Trade), [&](uint8_t* dst) {
    std::memcpy(dst, &trade, sizeof(Trade));
});
```

Queue 只负责：

``` text
给调用者 destination memory
```

调用者负责：

``` text
如何把具体 Event 写成 bytes
```

因此 Queue 本身不需要认识 `Trade / AddOrder / CancelOrder`。

------------------------------------------------------------------------

# 5. V1 Consumer：自己的 `lastIndex`

Consumer 可以有自己的 local state：

``` cpp
struct Reader
{
    uint64_t lastIndex = 0;
    uint8_t* current = nullptr;
};
```

检查有没有新数据：

``` cpp
if (lastIndex == queue.mIndex.load(std::memory_order_acquire)) {
    return; // no new data
}
```

这里非常容易误解。

这不是：

``` text
Producer 读取 Consumer index
```

而是：

``` text
Consumer:
    读取自己的 lastIndex
    +
    读取 Producer publish 的 mIndex
```

结构是：

``` text
Shared Queue:
    mIndex
    mPendingIndex

Consumer A local:
    lastIndexA

Consumer B local:
    lastIndexB

Consumer C local:
    lastIndexC
```

Producer 完全不会：

``` cpp
min(lastIndexA, lastIndexB, lastIndexC);
```

------------------------------------------------------------------------

# 6. V1 Pop：读取 + Overflow Detection

概念代码：

``` cpp
template <class ReadCallback>
bool Q::Pop(Reader& reader, ReadCallback readCallback)
{
    const uint64_t published =
        mIndex.load(std::memory_order_acquire);

    if (reader.lastIndex == published) {
        return false; // no data
    }

    MessageSize size;

    // Read record size from the current record.
    std::memcpy(
        &size,
        reader.current,
        sizeof(MessageSize)
    );

    // Producer may already be writing far ahead of us.
    const uint64_t pending =
        mPendingIndex.load(std::memory_order_acquire);

    if (pending - reader.lastIndex > mCapacity) {
        // Consumer has fallen more than one ring behind.
        // Old data may already be overwritten.
        throw Overflow{};
    }

    std::vector<uint8_t> local(size);

    std::memcpy(
        local.data(),
        reader.current + sizeof(MessageSize),
        size
    );

    readCallback(local.data(), size);

    reader.lastIndex += sizeof(MessageSize) + size;

    // Advance reader.current and handle wrapping.

    return true;
}
```

真实 low-latency 实现不会在 `Pop()` hot path 每次创建
`std::vector`；这里仅用于表达"先 copy 到 Consumer-owned
memory"的思想。实际可以使用预分配 scratch buffer。

------------------------------------------------------------------------

# 7. 为什么 V1 要看 `mPendingIndex`？

`mIndex` 只告诉 Reader：

``` text
Producer 已经完整 publish 到哪里
```

但 Producer 可能正在覆盖更远的区域。

例如：

``` text
capacity = 1000

Consumer lastIndex = 5100

mIndex        = 5900
mPendingIndex = 6200
```

如果只看：

``` text
mIndex - lastIndex
= 800
```

似乎还没超过 capacity。

但 Producer 实际已经开始写到：

``` text
6200
```

所以：

``` text
mPendingIndex - lastIndex
= 1100
> 1000
```

Consumer 想读的 physical memory 已经可能正在被 Producer 覆盖。

因此：

``` text
mIndex
    → availability：哪些数据已经完整 publish

mPendingIndex
    → overwrite safety：Producer 实际已经开始碰到哪里
```

------------------------------------------------------------------------

# 8. V1 的核心缺点：Queue Header Contention

虽然 V1 已经做到：

``` text
Producer 不读取任何 Consumer lastIndex
```

但所有 Consumer 还是会不断读取：

``` text
mIndex
mPendingIndex
```

Producer 又不断写它们。

于是：

``` text
                  Consumer A
                     ↓
                  Consumer B
                     ↓
Producer ←→ [mIndex / mPendingIndex] ←→ Consumer C
                     ↑
                  Consumer D
```

这些 global atomic 所在的 cache line 会非常 hot。

因此 V2 的目标是：

> **把 atomic state 从 Queue Header 分散到每个 queue element / block。**

------------------------------------------------------------------------

# 9. SPMC V2：Per-Block Version

V2 的核心思想：

``` text
V1:

Global:
    mIndex
    mPendingIndex


V2:

Block 0:
    version

Block 1:
    version

Block 2:
    version

...
```

每个 Block 的 version 同时表示：

``` text
1. Producer 是否正在写这个 Block
2. 这个 Block 已经被 Producer 写过第几轮
```

规则：

``` text
偶数 → stable
奇数 → write in progress
```

生命周期：

``` text
0 → 1 → 2 → 3 → 4 → 5 → 6 ...

0 = initial

1 = 第一次正在写
2 = 第一次写完成

3 = 第二次正在写
4 = 第二次写完成

5 = 第三次正在写
6 = 第三次写完成
```

因此最低 bit：

``` cpp
version & 1
```

就可以判断 Producer 是否正在写。

------------------------------------------------------------------------

# 10. Version 如何检测 Overflow？

假设 Ring 有 4 个 physical Blocks：

``` text
Physical:
0 1 2 3
```

Producer logical stream：

``` text
Logical:   0 1 2 3 4 5 6 7 8 ...
Physical:  0 1 2 3 0 1 2 3 0 ...
```

第一次写完所有 slot：

``` text
[2][2][2][2]
```

Producer 绕回来重新覆盖 Block 0：

``` text
2 → 3 → 4
```

于是：

``` text
[4][2][2][2]
```

Consumer 如果期待：

``` text
expected version = 2
```

但看到：

``` text
actual version = 4
```

说明：

``` text
Producer 已经绕回来覆盖过这个 Block
→ Consumer 想要的旧 Event 已经丢失
→ OVERFLOW
```

所以 V1：

``` cpp
mPendingIndex - lastIndex > capacity
```

的 global overflow check，在 V2 中可以转化成：

``` text
这个 Block 的 version
是否仍然是 Consumer 期待的 generation？
```

------------------------------------------------------------------------

# 11. V2 Data Structure

Slide 中的结构：

``` cpp
using BlockVersion = uint32_t;
using MessageSize  = uint32_t;

struct Block
{
    std::atomic<BlockVersion> mVersion;
    std::atomic<MessageSize>  mSize;

    uint8_t mData[0];
};

struct Header
{
    alignas(64)
    std::atomic<uint64_t> mBlockCounter[0];

    alignas(64)
    Block mBlocks[0];
};
```

`mData[0]`、`mBlockCounter[0]`、`mBlocks[0]` 是 slide
使用的简化/编译器扩展式表达，重点是 memory
layout，不是建议直接照抄成标准 C++。

一个 Block：

``` text
┌──────────────────────┐
│ mVersion             │
├──────────────────────┤
│ mSize                │
├──────────────────────┤
│ mData...             │
└──────────────────────┘
```

三个状态不要混：

``` text
mBlockCounter
    Queue-level
    Producer 总共 publish 到哪个 logical Block
    Reader join queue 时用于定位

mVersion
    Block-level
    判断 generation / write-in-progress / overflow

Reader local position
    Consumer-local
    表示这个 Consumer 自己读到哪个 logical Block
```

------------------------------------------------------------------------

# 12. 为什么 V2 仍然有 `mBlockCounter`？

因为一个新 Reader 刚加入时，需要知道：

``` text
Producer 当前大概走到哪里？
```

例如：

``` text
mBlockCounter = 100000
```

新 Consumer：

``` cpp
uint64_t current =
    header.mBlockCounter.load(std::memory_order_acquire);
```

然后初始化自己的 local position。

重点是：

> **正常消费 hot path 不需要每条 Event 都读取 `mBlockCounter`。**

所以：

``` text
V1:
Block → Header → Block → Header → Block → Header...

V2:
Block → Block → Block → Block...
```

`mBlockCounter` 虽然 Producer 每次都会修改，但 Reader 通常只在 join
时读取，因此不会像 V1 的 header atomic 一样被所有 Reader 持续访问。

------------------------------------------------------------------------

# 13. V2 Producer Write

Slide 的简化代码：

``` cpp
template <class WriteCallback>
void Q::Write(MessageSize size, WriteCallback writeCallback)
{
    mVersion += 1;

    mSize = size;
    writeCallback(&mCurrentBlock->mData[0]);

    mVersion += 1;
    mBlockCounter += 1;

    // Advance and handle Wrapping
}
```

结合数据结构，更明确地写：

``` cpp
template <class WriteCallback>
void Q::Write(MessageSize size, WriteCallback writeCallback)
{
    Block& block = *mCurrentBlock;

    // 1. Mark block as being written.
    // even -> odd
    block.mVersion.fetch_add(
        1,
        std::memory_order_acq_rel
    );

    // 2. Write metadata + payload.
    block.mSize.store(
        size,
        std::memory_order_relaxed
    );

    writeCallback(&block.mData[0]);

    // 3. Publish complete block.
    // odd -> even
    block.mVersion.fetch_add(
        1,
        std::memory_order_release
    );

    // 4. Advance logical producer progress.
    mBlockCounter.fetch_add(
        1,
        std::memory_order_relaxed
    );

    // 5. Advance mCurrentBlock and handle wrapping.
}
```

核心过程：

``` text
version = 2
    ↓
version = 3       odd
    ↓
WRITE IN PROGRESS
    ↓
write size
    ↓
write data
    ↓
version = 4       even
    ↓
BLOCK PUBLISHED
    ↓
mBlockCounter++
```

第一次使用 Block：

``` text
0 → 1 → 2
```

第二次绕回来：

``` text
2 → 3 → 4
```

第三次：

``` text
4 → 5 → 6
```

------------------------------------------------------------------------

# 14. V2 Consumer Read：Seqlock 思想

Consumer 的核心读取逻辑类似 Seqlock：

``` cpp
template <class ReadCallback>
bool ReadBlock(
    Block& block,
    BlockVersion expectedVersion,
    ReadCallback readCallback)
{
    // 1. Read version before touching payload.
    BlockVersion before =
        block.mVersion.load(std::memory_order_acquire);

    // Producer is currently modifying this block.
    if (before & 1) {
        return false; // retry later
    }

    // Wrong generation:
    // producer may have wrapped and overwritten our event.
    if (before != expectedVersion) {
        throw Overflow{};
    }

    MessageSize size =
        block.mSize.load(std::memory_order_relaxed);

    // In real low-latency code, use a preallocated local buffer.
    std::vector<uint8_t> local(size);

    std::memcpy(
        local.data(),
        &block.mData[0],
        size
    );

    // 2. Re-read version after copying.
    BlockVersion after =
        block.mVersion.load(std::memory_order_acquire);

    // Producer changed the block while we were copying.
    if (before != after || (after & 1)) {
        return false; // retry / treat as invalid
    }

    readCallback(local.data(), size);
    return true;
}
```

核心不是这份示例代码的具体 API，而是：

``` text
read version before
        ↓
odd?
├── yes → Producer 正在写，retry
↓ no
version 是我期待的 generation？
├── no → overflow
↓ yes
copy size + data
        ↓
read version after
        ↓
before == after ?
├── no → copy 期间被 Producer 修改，数据无效
↓ yes
safe → callback
```

这就是：

> **每个 Block 上一个 mini Seqlock。**

------------------------------------------------------------------------

# 15. 为什么一定要读取两次 Version？

假设 Consumer：

``` text
read version = 2
```

然后准备 copy。

但这时 Producer 绕回来：

``` text
Consumer                     Producer

read version = 2

                             2 → 3
                             begin overwrite

copy data  ← 此时可能读到混合数据

                             write...
                             3 → 4

read version = 4
```

Consumer 最后发现：

``` text
before = 2
after  = 4
```

所以：

``` text
刚才 copy 的数据不可信
```

如果只在 copy 前看一次 `version == 2`，无法防止 Producer **恰好在 copy
过程中开始覆盖**。

------------------------------------------------------------------------

# 16. V1 vs V2

  -----------------------------------------------------------------------
                          SPMC V1                 SPMC V2
  ----------------------- ----------------------- -----------------------
  Producer 是否读取       否                      否
  Consumer progress                               

  Consumer 是否有 local   是                      是
  position                                        

  正常 Reader 是否频繁读  是                      尽量否
  global header                                   

  Publish state           `mIndex`                per-Block `mVersion`

  In-progress state       `mPendingIndex`         `mVersion` odd

  Overflow detection      global pending distance expected vs actual
                                                  Block version

  Contention              Queue Header 较 hot     state 分散到各 Block

  Slow Consumer           无                      无
  backpressure                                    

  Slow Consumer 可能      是                      是
  overflow                                        
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# 17. 从 V1 到 V2 的演化

V1：

``` text
                       Consumer A
                           │
                       Consumer B
                           │
                           ▼
Producer ←──── [mIndex / mPendingIndex]
                           ▲
                           │
                       Consumer C
                           │
                       Consumer D
```

问题：

``` text
所有 Reader 都频繁访问同一组 global atomic
→ hot cache line
→ cache coherence traffic
```

V2：

``` text
Header:
    mBlockCounter
         ↑
    Reader join 时主要读取


Hot Path:

Producer     → Block 103.version
Consumer A  → Block 100.version
Consumer B  → Block 101.version
Consumer C  → Block 102.version
```

于是 contention：

``` text
global
   ↓
localized per block
```

------------------------------------------------------------------------

# 18. 最终面试版总结

如果面试官问：

> **How would you design a low-latency SPMC market-data broadcast
> queue?**

可以从简单版本逐步优化：

``` text
1. Ring Buffer + one local read position per Consumer.

2. 不让 Producer 读取 Consumer read positions。
   Slow Consumer 不产生 backpressure。

3. V1:
   global mIndex
       → 已经 publish 到哪里

   global mPendingIndex
       → Producer 正在写到哪里

   Consumer 自己检测 overflow。

4. V1 问题：
   所有 Consumers 频繁读取 global atomics，
   Queue Header cache line 很 hot。

5. V2:
   把状态分散到每个 Block。

   Each Block:
       version
       size
       data

6. version:
   odd  → writer in progress
   even → stable

   generation change
       → detect overwrite / overflow

7. Reader:
   read version
   copy data
   read version again

   version unchanged
       → valid snapshot

8. Global mBlockCounter:
   主要给新 Reader join 时定位，
   不放在正常 Consumer hot path。

9. Trade-off:
   Producer 永远不等 slow Consumer，
   所以 slow Consumer 可能 overflow，
   overflow 后需要 snapshot / resync / restart。
```

一句话总结：

> **V1 用 global publish/pending counters 实现 non-blocking
> broadcast；V2 再把同步状态下沉到每个 Block，用 per-block
> version/seqlock 检测 write-in-progress 和 overflow，从而进一步减少
> Queue Header contention。**
