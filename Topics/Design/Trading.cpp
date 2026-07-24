class Account {
  public:
    Account(int accountId, double cash): accountId_(accountId), cash_(cash) {}

    bool buy(const std::string& stock, int quantity, double price) {
        if (quantity <= 0 || price <= 0) { return false; }
      
        std::lock_guard<std::mutex> lock(mutex_);
        double totalCost = quantity*price;
        if (cash_ < totalCost) { return false; }
        cash_ -= totalCost;
        positions_[symbol] += quantity;
        return true;
    }

    bool sell(const std::string& stock, int quantity, double price) {
        if (quantity <= 0 || price <= 0) { return false; }
      
        std::lock_guard<std::mutex> lock(mutex_);
        auto it = positions_.find(symbol);
        if (it  == positions_.end() || it->second < quantity) { return false; }
        it->second -= quantity;
        cash_ += quantity * price;
        if (it->second == 0) { positions_.erase(it); }
        return true;
    }

  private:
    int accountId_;
    double cash_;
    std::unordered_map<std::string, int> positions_;
    std::mutex mutex_;
}

class TradingSystem {
  public:
    void addAccount(int accountId, double cash) {
        std::lock_guard<std::mutex> lock(accountsMutex_);
        accounts_[accountId] = std::unique_ptr<Account>(accountId, cash);
    }

    bool buy(int accountId, const std::string& stock, int quantity, double price) {
        auto account = getAccount(accountId);
        if (!account) { return false; }
        return account->buy(symbol, quantity, price);
    }

    bool sell(int accountId, const std::string& stock, int quantity, double price) {
        auto account = getAccount(accountId);
        if (!account) { return false; }
        return account->sell(symbol, quantity, price);
    }

  private:
    std::unique_ptr<Account> getAccount(int accountId) {
        std::lock_guard<std::mutex> lock(accountsMutex_);

        auto it = accounts_.find(accountId);
        if (it == accounts_.end()) { return nullptr; }
        return it->second;
    }

    std::unordered_map<int, std::unique_ptr<Account>> accounts_;
    std::mutex accountsMutex_;
}

// 两个线程同时卖同一只股票，为什么不会超卖？因为检查和扣减在同一把锁里
// 两个线程操作不同账户，能不能并发？ok 因为每个Account有自己的mutex：
// 同一个账户买AAPL、卖MSFT，为什么还要互相阻塞？因为当前是账户级锁 同时保护cash,position
// 为什么不用std::atomic<int>保存持仓？因为一次卖出包含多个步骤 不是单个整数自增自减。
// avoid deadlock? action on multiple accounts: std::scoped_lock lock(accountA.mutex_, accountB.mutex_);
double getCash() const { => mutable std::mutex mutex_;
    std::lock_guard<std::mutex> lock(mutex_);
    return cash_;
}






