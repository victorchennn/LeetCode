enum class Direction { Up, Down,  Idle   };

class Elevator {
private:
    void chooseDirection() {
        if (!upStops_.empty()) {
            direction_ = Direction::Up;
        } else if (!downStops_.empty()) {
            direction_ = Direction::Down;
        }
    }

    void openDoor() const {
        std::cout << "Elevator " << id_
                  << " stops at floor " << currentFloor_ << '\n';
    }

    int id_;
    int currentFloor_;
    Direction direction_;

    std::set<int> upStops_;
    std::set<int, std::greater<int>> downStops_;

public:
    Elevator(int id, int floor = 0)
        : id_(id), currentFloor_(floor), direction_(Direction::Idle) {}

    int id() const { return id_; }
    int currentFloor() const { return currentFloor_; }
    Direction direction() const { return direction_; }

    bool isIdle() const { return direction_ == Direction::Idle; }

    bool isMovingToward(int floor, Direction requestDirection) const {
        if (direction_ != requestDirection) {
            return false;
        }

        if (direction_ == Direction::Up) {
            return currentFloor_ <= floor;
        }
        return currentFloor_ >= floor;
    }

    void addStop(int floor) {
        if (floor > currentFloor_) {
            upStops_.insert(floor);
        } else if (floor < currentFloor_) {
            downStops_.insert(floor);
        }

        if (direction_ == Direction::Idle) {
            if (floor > currentFloor_) {
                direction_ = Direction::Up;
            } else if (floor < currentFloor_) {
                direction_ = Direction::Down;
            }
        }
    }

    void step() {
        if (direction_ == Direction::Idle) {
            chooseDirection();
        }

        if (direction_ == Direction::Up) {
            ++currentFloor_;

            if (upStops_.erase(currentFloor_)) {
                openDoor();
            }

            if (upStops_.empty()) {
                direction_ = downStops_.empty()
                    ? Direction::Idle
                    : Direction::Down;
            }
        } else if (direction_ == Direction::Down) {
            --currentFloor_;

            if (downStops_.erase(currentFloor_)) {
                openDoor();
            }

            if (downStops_.empty()) {
                direction_ = upStops_.empty()
                    ? Direction::Idle
                    : Direction::Up;
            }
        }
    }
};

class ElevatorController {
private:
    Elevator& selectElevator(int floor, Direction direction) {
        Elevator* best = nullptr;
        int bestScore = std::numeric_limits<int>::max();

        for (Elevator& elevator : elevators_) {
            int distance = std::abs(elevator.currentFloor() - floor);
            int score;

            if (elevator.isMovingToward(floor, direction)) {
                score = distance;
            } else if (elevator.isIdle()) {
                score = 1000 + distance;
            } else {
                score = 2000 + distance;
            }

            if (score < bestScore) {
                bestScore = score;
                best = &elevator;
            }
        }

        return *best;
    }

    std::vector<Elevator> elevators_;

public:
    ElevatorController(int elevatorCount, int initialFloor = 0) {
        for (int id = 0; id < elevatorCount; ++id) {
            elevators_.emplace_back(id, initialFloor);
        }
    }

    int requestElevator(int floor, Direction direction) {
        Elevator& elevator = selectElevator(floor, direction);
        elevator.addStop(floor);
        return elevator.id();
    }

    void selectFloor(int elevatorId, int floor) {
        elevators_.at(elevatorId).addStop(floor);
    }

    void step() {
        for (Elevator& elevator : elevators_) {
            elevator.step();
        }
    }


};
