P2P communication system design
```
Client A
   |
   | 1. contact server / login / find B
   v
Signaling Server
   |
   | 2. tell A how to reach B
   | 3. tell B how to reach A
   v
Client B

然后：

A <-------- direct P2P --------> B
       text / video packets
```

1. Text message 和 video call 用什么协议
因为 text message 一般要求：消息不能丢 / 顺序不能乱 / 重复消息要处理
TCP 自带：reliable delivery / ordered delivery / retransmission / congestion control

Video call：UDP 更适合 Video is latency-sensitive rather than loss-sensitive.
use sequenceNumber to detect frame loss, timestamps latency and jitter.

packet loss? 方法一：直接丢掉 late packet ≈ useless packet
方法二：降低 bitrate / video quality 说明网络可能扛不住当前 bitrate。 adaptive

方法三：Selective retransmission 这个 packet 现在重传还来得及吗？retransmit

2. 为什么 client 要先联系 server？ Signaling Server 它不是负责传视频的，而是帮双方建立连接
* Step 1：Bob login Bob → Server LOGIN userId = Bob | Bob -> online Bob -> connection/session information
* Step 2：Alice login Alice → Server LOGIN Alice | server 知道：Alice online Bob online
* Step 3：Alice call Bob | Alice → Signaling Server | server 找 Bob 当前连接：Bob → socket/session 123 ACCEPT
* Step 4：交换 connection information server 帮 A/B 交换：IP port connection candidates Alice <====== UDP ======> Bob 视频就不需要继续走 server。
signaling 和 media 分离 The server is mainly used for discovery and signaling

3. 但是为什么不能直接 A → B？ private IP / router 有一个 public IP Alice 自己不知道自己的公网地址。所以找一个公网 server：Hello, what IP/port do you see me as?
于是 signaling server 可以帮助交换：Alice public endpoint <-> Bob public endpoint

NAT allows multiple devices with private IP addresses to share a public IP address. The router maintains mappings between private IP/port pairs and public IP/port pairs.
Because a client behind NAT doesn't have a directly reachable public IP and port. Its private IP is not routable over the Internet, and the NAT may also block unsolicited incoming connections.

4. 对方 offline，text message 怎么办？ server 应该持久化：Message Store  Alice- TCP- Server Message Store- TCP- Bob // store-and-forward

5. Video call 中 Alice 突然换网络怎么办？IP:port NAT mapping全部变了 P2P connection breaks 需要重新：discover network endpoint - connectivity check - update peer
- resume media

6. Server 挂了怎么办？
