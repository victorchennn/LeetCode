// Users
//   |
//   v
// +------------------+
// | Virtual Waiting  |
// | Room             |
// +--------+---------+
//          |
//          v
// +------------------+
// | Load Balancer    | 把请求分给不同 Server
// +--------+---------+
//          |
//    +-----+--------+
//    |              |
//    v              v
// Event Service   Booking Service
//    |              |
//    v              |
//  DB - Cache       |
//                    |
//                    v
//               Primary DB
//                    |
//                    v
//             Payment Service

// Event Service(“大概现在还能买什么”): read-heavy Event Service 里的数据分成两类  
// 第一类几乎永远不变：stadium layout section row seat coordinates event name event start time 静态数据很好 cache：
// 第二类经常变化：AVAILABLE HELD SOLD price 动态 availability 也 cache TTL要短一点
//              ┌── Event Service 1 ──┐ stateless service
//              │                     │
// User → LB ───┼── Event Service 2 ──┼──> Shared Cache
//              │                     │
//              └── Event Service 3 ──┘
//                                       |
//                                       v
//                                    Database

enum class ReservationStatus {
    Active, Cancelled
};

struct Seat {int row; int number;};

struct Reservation {
    int id;
    int showtimeId;
    std::vector<Seat> seats;
    ReservationStatus status = ReservationStatus::Active;
};

class Showtime {
    private: 
        int id_;
        std::string movie_;
        std::vector<std::vector<bool>> available_;
        mutable std::mutex mutex_;

        bool isValid(const Seat& seat) const {
            return seat.row >= 0 &&
                seat.row < static_cast<int>(available_.size()) &&
                seat.number >= 0 &&
                seat.number < static_cast<int>(available_[seat.row].size());
        }
    
    public:
        Showtime(int id, std::string movie, int rows, int seats)
            : id_(id), movie_(std::move(movie)), 
            available_(rows, std::vector<bool>(seats, true)) {}
        
        bool reserveSeats(const std::vector<Seat>& seats) {
            std::lock_guard<std::mutex> lock(mutex_);

            for (const Seat& seat: seats) {
                if (!isValid(seat) || !available_[seat.row][seat.number]) {
                    return false;
                }
            }

            for (const Seat& seat: seats) {
                available_[seat.row][seat.number] = false;
            }
            return true;
        }

        bool releaseSeats(const std::vector<Seat>& seats) {
            std::lock_guard<std::mutex> lock(mutex_);

            for (const Seat& seat: seats) {
                if (!isValid(seat) || available_[seat.row][seat.number]) {
                    return false;
                }
            }

            for (const Seat& seat: seats) {
                available_[seat.row][seat.number] = true;
            }
            return true;
        }

        std::vector<Seat> getAvailableSeats() const {
            std::lock_guard<std::mutex> lock(mutex_);

            std::vector<Seat> result;
            for (int row = 0; row < static_cast<int>(available_.size()); row++) {
                for (int number = 0; number < static_cast<int>(available_[row].size()); number++) {
                    if (available_[row][number]) {
                        result.push_back({row, number});
                    } 
                }
            }
            return result;
        }
};

class BookingSystem {
    private:
        std::unordered_map<int, Showtime> showtimes_;
        std::unordered_map<int, Reservation> reservations_;

        std::atomic<int> reservationId_{1};
        std::mutex systemMutex_;

    public:
        void addShowtime(int showtimeId, const std::string& movie, int rows, int seatsPerRow) {
            std::lock_guard<std::mutex> lock(systemMutex_);
            showtimes_.try_emplace(showtimeId, showtimeId, movie, rows, seatsPerRow);
        }

        int bookSeats(int showtimeId, const std::vector<Seat>& seats) {
            Showtime* showtime;
            {
                std::lock_guard<std::mutex> lock(systemMutex_);
                // showtimes_ 是整个系统共享的数据：多个线程可 add/remove/book 所以必须保护。
                auto it = showtimes_.find(showtimeId);
                if (it == showtimes_.end()) {
                    return -1;
                }
                showtime = &it->second;
            }

            // no need to lock all because different shows
            if (!showtime->reserveSeats(seats)) {
                return -1;
            }

            int reservationId = reservationId_.fetch_add(1);
            {
                std::lock_guard<std::mutex> lock(systemMutex_);
                reservations_.emplace(reservationId, // Reservation is a struct, does not have constructor
                    Reservation{reservationId, showtimeId, seats, ReservationStatus::Active});
            }
            return reservationId;
        }

        bool cancelReservation(int reservationId) {
            std::lock_guard<std::mutex> lock(systemMutex_);
            auto it = reservations_.find(reservationId);
            if (it == reservations_.end()) {
                return false;
            }

            Reservation& reservation = it->second;
            if (reservation.status == ReservationStatus::Cancelled) {
                return false;
            }

            auto showtimeIt = showtimes_.find(reservation.showtimeId);
            if (showtimeIt == showtimes_.end()) {
                return false;
            }

            if (!showtimeIt->second.releaseSeats(reservation.seats)) {
                return false;
            }
            reservation.status = ReservationStatus::Cancelled;
            return true;
        }
};

// 1. 如果有 1 million users 同时抢票：
//    不要让所有请求直接打到 bookSeats(eventId, seats)
//    前面加 Virtual Waiting Room / Rate Limiting，控制进入 Booking Service 的流量。

// 2. 单机锁粒度可以继续优化：
//    现在是 per-Showtime mutex。
//    如果同一个 Showtime 并发仍然很高，可以进一步按 Section / Row 分锁，
//    让不同区域的订票并行执行。

// 3. 浏览座位图的流量远大于真正购买：
//    不能让用户每次刷新座位图都读取 Primary Database。
//    
//    Seat Map 静态结构 -> CDN
//
//    大致的座位可用状态 -> Redis / Cache
//
//    真正 hold / confirm seat
//        -> Booking Service
//        -> Primary Database / authoritative storage

// 4. Redis：
//    高速内存数据存储，读写很快。
//    很适合保存 temporary seat hold，例如：
//        seat A1 -> reservation 100
//        TTL = 10 minutes
//
//    可以使用原子操作，例如 SET NX + TTL，
//    防止多个 Booking Server 同时 hold 同一个 seat。

// 5. CDN:
//    Content Delivery Network，内容分发网络。
//    把静态内容放到离用户更近的 edge server，
//    例如 event 图片、venue layout、静态 seat map，减少源站压力和访问延迟。

// 6. Kafka：
//    高吞吐的分布式 event streaming / message system。
//    更适合 booking 成功后的异步事件，而不是决定谁抢到 seat。
//
//    Booking confirmed
//        -> Kafka
//            -> Email Service
//            -> Notification Service
//            -> Analytics
//            -> Fraud Detection

// next version
enum class SeatStatus { Available, Held, Booked };
enum class ReservationStatus { Held, Booked, Cancelled }; // , Expired

struct SeatState {
    SeatStatus status = SeatStatus::Available;
    int reservationId = -1;
};

struct Reservation {
    int id;
    int showtimeId;
    std::vector<Seat> seats;
    ReservationStatus status = ReservationStatus::Held;

    // std::chrono::steady_clock::time_point expiresAt;
};

// static constexpr auto HOLD_DURATION = std::chrono::minutes(10);

class Showtime {
private:
    std::vector<std::vector<SeatState>> seats_;

public:
    Showtime(int id, std::string movie, int rows, int seats)
            : id_(id), movie_(std::move(movie)), 
            seats_(rows, std::vector<SeatState>(seatsPerRow)) {}

    // Available -> Held
    bool holdSeats(const std::vector<Seat>& seats, int reservationId) {
        std::lock_guard<std::mutex> lock(mutex_);

        for (const Seat& seat : seats) {
            if (!isValid(seat)) {
                return false;
            }

            const SeatState& state = seats_[seat.row][seat.number];
            if (state.status != SeatStatus::Available) {
                return false;
            }
        }

        for (const Seat& seat : seats) {
            SeatState& state = seats_[seat.row][seat.number];

            state.status = SeatStatus::Held;
            state.reservationId = reservationId;
        }
        return true;
    }

    // Held -> Booked
    bool confirmSeats(const std::vector<Seat>& seats, int reservationId) {
        std::lock_guard<std::mutex> lock(mutex_);

        for (const Seat& seat : seats) {
            if (!isValid(seat)) {
                return false;
            }

            const SeatState& state = seats_[seat.row][seat.number];
            if (state.status != SeatStatus::Held || state.reservationId != reservationId) {
                return false;
            }
        }

        for (const Seat& seat : seats) {
            SeatState& state = seats_[seat.row][seat.number];
            state.status = SeatStatus::Booked;
        }
        return true;
    }

    // Held -> Available
    bool releaseHeldSeats(const std::vector<Seat>& seats, int reservationId) {
        std::lock_guard<std::mutex> lock(mutex_);

        for (const Seat& seat : seats) {
            if (!isValid(seat)) {
                return false;
            }

            const SeatState& state = seats_[seat.row][seat.number];
            if (state.status != SeatStatus::Held || state.reservationId != reservationId) {
                return false;
            }
        }

        for (const Seat& seat : seats) {
            SeatState& state = seats_[seat.row][seat.number];

            state.status = SeatStatus::Available;
            state.reservationId = -1;
        }
        return true;
    }
};

class BookingSystem {
public:
    // User selects seats: Available -> Held
    int holdSeats(int showtimeId, const std::vector<Seat>& seats) {
        Showtime* showtime;
        {
            std::lock_guard<std::mutex> lock(systemMutex_);

            auto it = showtimes_.find(showtimeId);
            if (it == showtimes_.end()) {
                return -1;
            }
            showtime = &it->second;
        }

        int reservationId = reservationId_.fetch_add(1);
        if (!showtime->holdSeats(seats, reservationId)) {
            return -1;
        }

        {
            std::lock_guard<std::mutex> lock(systemMutex_);
            reservations_.emplace(reservationId,
                Reservation{reservationId, showtimeId, seats, ReservationStatus::Held}); 
                // , std::chrono::steady_clock::now() + HOLD_DURATION
        }
        return reservationId;
    }

    // Payment succeeds: Held -> Booked
    bool confirmReservation(int reservationId) {
        std::lock_guard<std::mutex> lock(systemMutex_);

        auto it = reservations_.find(reservationId);
        if (it == reservations_.end()) {
            return false;
        }

        Reservation& reservation = it->second;
        if (reservation.status != ReservationStatus::Held) {
            return false;
        }

        auto showtimeIt = showtimes_.find(reservation.showtimeId);
        if (showtimeIt == showtimes_.end()) {
            return false;
        }

        // auto now = std::chrono::steady_clock::now();
        // if (now > reservation.expiresAt) {
        //     showtimeIt->second.releaseHeldSeats(reservation.seats, reservationId);

        //     reservation.status = ReservationStatus::Expired;
        //     return false;
        // }

        if (!showtimeIt->second.confirmSeats(reservation.seats, reservationId)) {
            return false;
        }

        reservation.status = ReservationStatus::Booked;
        return true;
    }

    // User cancels before payment: Held -> Available
    bool cancelReservation(int reservationId) {
        std::lock_guard<std::mutex> lock(systemMutex_);

        auto it = reservations_.find(reservationId);
        if (it == reservations_.end()) {
            return false;
        }

        Reservation& reservation = it->second;
        if (reservation.status != ReservationStatus::Held) {
            return false;
        }

        auto showtimeIt = showtimes_.find(reservation.showtimeId);
        if (showtimeIt == showtimes_.end()) {
            return false;
        }

        if (!showtimeIt->second.releaseHeldSeats(reservation.seats, reservationId)) {
            return false;
        }

        reservation.status = ReservationStatus::Cancelled;
        return true;
    }
 
    // 周期性调用 比如每秒一次? 
    // void cleanupExpiredReservations() {
    //     std::lock_guard<std::mutex> lock(systemMutex_);

    //     auto now = std::chrono::steady_clock::now();
    //     for (auto& [id, reservation] : reservations_) { // better use priority_queue based on expiresAt
    //         if (reservation.status != ReservationStatus::Held) {
    //             continue;
    //         }

    //         if (now < reservation.expiresAt) {
    //             continue;
    //         }

    //         auto showtimeIt = showtimes_.find(reservation.showtimeId);
    //         if (showtimeIt == showtimes_.end()) {
    //             continue;
    //         }

    //         if (showtimeIt->second.releaseHeldSeats(reservation.seats, reservation.id)) {
    //             reservation.status = ReservationStatus::Expired;
    //         }
    //     }
    // }
};




