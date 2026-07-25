class SupermarketCheckout {
private:
    struct Customer {
        int id;
        int totalItems;
        int processedItems = 0;

        int remainingItems() const {
            return totalItems - processedItems;
        }
    };

    using CustomerQueue = std::list<Customer>;

    struct CustomerLocation {
        int lineNumber;
        CustomerQueue::iterator iterator;
    };

    // lineNumber -> FIFO customer queue
    // std::map keeps lines ordered by line number.
    std::map<int, CustomerQueue> lines_;

    // customerId -> current line and exact position
    std::unordered_map<int, CustomerLocation> customerIndex_;

    // Customers in checkout order
    std::vector<int> exitOrder_;

    void onCustomerExit(int customerId) {
        exitOrder_.push_back(customerId);
    }

    void removeCustomer(int customerId, int lineNumber, CustomerQueue::iterator customerIt) {
        auto lineIt = lines_.find(lineNumber);
        if (lineIt == lines_.end()) {
            return;
        }

        lineIt->second.erase(customerIt);
        customerIndex_.erase(customerId);

        if (lineIt->second.empty()) {
            lines_.erase(lineIt);
        }

        onCustomerExit(customerId);
    }

    void processLine(int lineNumber, int numProcessedItems) {
        if (numProcessedItems <= 0) {
            return;
        }

        auto lineIt = lines_.find(lineNumber);
        if (lineIt == lines_.end()) {
            return;
        }

        CustomerQueue& line = lineIt->second;

        while (numProcessedItems > 0 && !line.empty()) {
            Customer& customer = line.front();

            int processedNow = std::min(numProcessedItems, customer.remainingItems());

            customer.processedItems += processedNow;
            numProcessedItems -= processedNow;

            if (customer.remainingItems() <= 0) {
                int customerId = customer.id;

                customerIndex_.erase(customerId);
                line.pop_front();

                onCustomerExit(customerId);
            }
        }

        if (line.empty()) {
            lines_.erase(lineIt);
        }
    }

public:
    void customerEnter(int customerId, int lineNumber, int initialNumItems) {
        if (customerIndex_.contains(customerId)) {
            throw std::invalid_argument("Customer already exists");
        }

        if (initialNumItems <= 0) {
            onCustomerExit(customerId);
            return;
        }

        CustomerQueue& line = lines_[lineNumber];
        line.push_back(Customer{customerId, initialNumItems, 0});

        auto customerIt = std::prev(line.end());
        customerIndex_[customerId] = CustomerLocation{lineNumber,customerIt};
    }

    void basketChange(int customerId, int newNumItems) {
        auto indexIt = customerIndex_.find(customerId);
        if (indexIt == customerIndex_.end()) {
            throw std::invalid_argument("Customer does not exist");
        }

        int lineNumber = indexIt->second.lineNumber;
        auto customerIt = indexIt->second.iterator;

        CustomerQueue& line = lines_.at(lineNumber);
        Customer& customer = *customerIt;

        int oldTotalItems = customer.totalItems;
        customer.totalItems = newNumItems;

        // The new total includes already processed items.
        if (customer.remainingItems() <= 0) {
            removeCustomer(customerId, lineNumber, customerIt);
            return;
        }

        // Adding items loses the current queue priority.
        if (newNumItems > oldTotalItems) {
            line.splice(line.end(), line, customerIt);
        }
    }

    void lineService(int lineNumber, int numProcessedItems) {
        processLine(lineNumber, numProcessedItems);
    }

    void linesService() {
        for (const auto& [lineNumber, line] : lines_) {
            if (!line.empty()) {
                processLine(lineNumber, 1);
            }
        }
    }

    const std::vector<int>& getExitOrder() const {
        return exitOrder_;
    }
};

// 如果顾客数量非常大怎么办？std::list .. memory pool
// 如果 BasketChange 很少发生呢？deque
