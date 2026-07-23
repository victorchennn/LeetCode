enum class ReservationStatus {
    Active,
    Cancelled
};

struct Seat {
    int row;
    int number;
};

struct Reservation {
    long long id;
    int showtimeId;
    std::vector<Seat> seats;
    ReservationStatus status = ReservationStatus::Active;
};

class Showtime {
public:
    Showtime(int id, std::string movie, int rows, int seatsPerRow)
        : id_(id),
          movie_(std::move(movie)),
          available_(rows, std::vector<bool>(seatsPerRow, true)) {}

    bool reserveSeats(const std::vector<Seat>& seats) {
        std::lock_guard<std::mutex> lock(mutex_);

        for (const Seat& seat : seats) {
            if (!isValid(seat) || !available_[seat.row][seat.number]) {
                return false;
            }
        }

        for (const Seat& seat : seats) {
            available_[seat.row][seat.number] = false;
        }

        return true;
    }

    bool releaseSeats(const std::vector<Seat>& seats) {
        std::lock_guard<std::mutex> lock(mutex_);

        for (const Seat& seat : seats) {
            if (!isValid(seat) || available_[seat.row][seat.number]) {
                return false;
            }
        }

        for (const Seat& seat : seats) {
            available_[seat.row][seat.number] = true;
        }

        return true;
    }

    std::vector<Seat> getAvailableSeats() const {
        std::lock_guard<std::mutex> lock(mutex_);

        std::vector<Seat> result;

        for (int row = 0; row < static_cast<int>(available_.size()); ++row) {
            for (int number = 0;
                 number < static_cast<int>(available_[row].size());
                 ++number) {
                if (available_[row][number]) {
                    result.push_back({row, number});
                }
            }
        }

        return result;
    }

private:
    bool isValid(const Seat& seat) const {
        return seat.row >= 0 &&
               seat.row < static_cast<int>(available_.size()) &&
               seat.number >= 0 &&
               seat.number < static_cast<int>(available_[seat.row].size());
    }

    int id_;
    std::string movie_;
    std::vector<std::vector<bool>> available_;

    mutable std::mutex mutex_;
};


class BookingSystem {
public:
    void addShowtime(int showtimeId,
                     const std::string& movie,
                     int rows,
                     int seatsPerRow) {
        std::lock_guard<std::mutex> lock(systemMutex_);

        showtimes_.try_emplace(
            showtimeId,
            showtimeId,
            movie,
            rows,
            seatsPerRow
        );
    }

    long long bookSeats(int showtimeId,
                        const std::vector<Seat>& seats) {
        Showtime* showtime = nullptr;

        {
            std::lock_guard<std::mutex> lock(systemMutex_);

            auto it = showtimes_.find(showtimeId);
            if (it == showtimes_.end()) {
                return -1;
            }

            showtime = &it->second;
        }

        if (!showtime->reserveSeats(seats)) {
            return -1;
        }

        long long reservationId = nextReservationId_.fetch_add(1);

        {
            std::lock_guard<std::mutex> lock(systemMutex_);

            reservations_.emplace(
                reservationId,
                Reservation{
                    reservationId,
                    showtimeId,
                    seats,
                    ReservationStatus::Active
                }
            );
        }

        return reservationId;
    }

    bool cancelReservation(long long reservationId) {
        std::lock_guard<std::mutex> lock(systemMutex_);

        auto reservationIt = reservations_.find(reservationId);
        if (reservationIt == reservations_.end()) {
            return false;
        }

        Reservation& reservation = reservationIt->second;

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

private:
    std::unordered_map<int, Showtime> showtimes_;
    std::unordered_map<long long, Reservation> reservations_;

    std::atomic<long long> nextReservationId_{1};
    std::mutex systemMutex_;
};






