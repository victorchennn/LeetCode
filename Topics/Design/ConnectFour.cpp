enum class GameState { IN_PROGRESS, WON, DRAW};
enum class DiscColor { RED, YELLOW };

class Player;
class Board;

class Game {
  private:
      Board board;
      Player player1;
      Player player2;
      Player* currentPlayer;
      GameState state;
      Player* winner;

public:
    Game(const Player& p1, const Player& p2)
        : board(),
          player1(p1),
          player2(p2),
          currentPlayer(&player1),
          state(GameState::IN_PROGRESS),
          winner(nullptr) {}

    bool makeMove(const Player& player, int column) {
        if (state != GameState::IN_PROGRESS) {
            return false;
        }
        if (&player != currentPlayer) {
            return false;
        }

        int row = board.placeDisc(column, player.getColor());
        if (row == -1) {
            return false;
        }

        if (board.checkWin(row, column, player.getColor())) {
            state = GameState::WON;
            winner = currentPlayer;
        } else if (board.isFull()) {
            state = GameState::DRAW;
        } else {
            currentPlayer = (currentPlayer == &player1) ? &player2 : &player1;
        }
        return true;
    }

    const Player* getCurrentPlayer() const {
        return currentPlayer;
    }

    GameState getGameState() const {
        return state;
    }

    const Player* getWinner() const {
        return winner;
    }

    const Board& getBoard() const {
        return board;
    }
};


enum class DiscColor { RED, YELLOW };

class Board {
private:
    static const int ROWS = 6;
    static const int COLS = 7;
    std::vector<std::vector<std::optional<DiscColor>>> grid;

    bool inBounds(int row, int column) const {
        return row >= 0 && row < ROWS && column >= 0 && column < COLS;
    }

    int countInDirection(int row, int column, int dr, int dc, DiscColor color) const {
        int count = 0;
        int r = row + dr;
        int c = column + dc;

        while (inBounds(r, c) && grid[r][c].has_value() && grid[r][c].value() == color) {
            count++;
            r += dr;
            c += dc;
        }

        return count;
    }

public:
    Board() : grid(ROWS, std::vector<std::optional<DiscColor>>(COLS, std::nullopt)) {}

    int getRows() const {
        return ROWS;
    }

    int getCols() const {
        return COLS;
    }

    bool canPlace(int column) const {
        if (column < 0 || column >= COLS) {
            return false;
        }
        return !grid[0][column].has_value();
    }

    int placeDisc(int column, DiscColor color) {
        if (!canPlace(column)) {
            return -1;
        }

        for (int row = ROWS - 1; row >= 0; row--) {
            if (!grid[row][column].has_value()) {
                grid[row][column] = color;
                return row;
            }
        }

        return -1;
    }

    bool checkWin(int row, int column, DiscColor color) const {
        if (!inBounds(row, column) || !grid[row][column].has_value() || grid[row][column].value() != color) {
            return false;
        }

        int directions[][2] = {
            {0, 1},   // horizontal
            {1, 0},   // vertical
            {1, 1},   // diagonal down-right
            {-1, 1}   // diagonal up-right
        };

        for (const auto& dir : directions) {
            int count = 1;
            count += countInDirection(row, column, dir[0], dir[1], color);
            count += countInDirection(row, column, -dir[0], -dir[1], color);
            if (count >= 4) {
                return true;
            }
        }

        return false;
    }

    bool isFull() const {
        for (int c = 0; c < COLS; c++) {
            if (!grid[0][c].has_value()) {
                return false;
            }
        }
        return true;
    }

    std::optional<DiscColor> getCell(int row, int column) const {
        if (!inBounds(row, column)) {
            return std::nullopt;
        }
        return grid[row][column];
    }
};

class Player {
  private:
      std::string name;
      DiscColor color;
  
  public:
      Player(const std::string& name, DiscColor color)
          : name(name), color(color) {}
  
      std::string getName() const {
          return name;
      }
  
      DiscColor getColor() const {
          return color;
      }
};

