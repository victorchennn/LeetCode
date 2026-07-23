class StockTransaction {
private:
    struct Stock {
        long long weightedSum = 0;  // sum(price * volume)
        long long totalVolume = 0;
        std::map<int, int> history; // timestamp -> price

        double vwap() const {
            return totalVolume == 0
                ? 0.0
                : static_cast<double>(weightedSum) / totalVolume;
        }
    };

    // stockName -> stock information
    std::unordered_map<std::string, Stock> stocks_;

    // {VWAP, stockName}, ordered from largest VWAP to smallest
    std::set<
        std::pair<double, std::string>,
        std::greater<std::pair<double, std::string>>
    > ranking_;

public:
    void update(const std::string& stockName,
                int price,
                int volume,
                int timestamp) {
        auto it = stocks_.find(stockName);

        if (it != stocks_.end()) {
            // Remove old ranking before changing VWAP.
            ranking_.erase({it->second.vwap(), stockName});
        }

        Stock& stock = stocks_[stockName];

        stock.weightedSum +=
            static_cast<long long>(price) * volume;
        stock.totalVolume += volume;
        stock.history[timestamp] = price;

        // Insert the updated VWAP.
        ranking_.insert({stock.vwap(), stockName});
    }

    double getVWAP(const std::string& stockName) const {
        auto it = stocks_.find(stockName);

        if (it == stocks_.end()) {
            return 0.0;
        }

        return it->second.vwap();
    }

    // Latest price at or before timestamp.
    int getPrice(const std::string& stockName, int timestamp) const {
        auto stockIt = stocks_.find(stockName);

        if (stockIt == stocks_.end()) {
            return 0;
        }

        const auto& history = stockIt->second.history;
        auto it = history.upper_bound(timestamp);

        if (it == history.begin()) {
            return 0;
        }

        return std::prev(it)->second;
    }

    int getMaxPriceInRange(const std::string& stockName,
                           int startTime,
                           int endTime) const {
        auto stockIt = stocks_.find(stockName);

        if (stockIt == stocks_.end() || startTime > endTime) {
            return 0;
        }

        const auto& history = stockIt->second.history;
        auto begin = history.lower_bound(startTime);
        auto end = history.upper_bound(endTime);

        int maxPrice = 0;

        for (auto it = begin; it != end; ++it) {
            maxPrice = std::max(maxPrice, it->second);
        }

        return maxPrice;
    }

    std::vector<std::string> topK(int k) const {
        std::vector<std::string> result;

        for (const auto& [vwap, stockName] : ranking_) {
            if (static_cast<int>(result.size()) == k) {
                break;
            }

            result.push_back(stockName);
        }

        return result;
    }
};
