struct Requirement { 
    int maxPrice;
    int requiredSeats;
};

struct TicketMessage {
    std::optional<int> artistId;
    std::optional<int> ticketPrice;
    std::optional<int> availableSeats;

    bool complete() const {
        return artistId.has_value() && ticketPrice.has_value() && availableSeats.has_value();
    }
};

class TicketMatcher {
private:
    
    std::unordered_map<int, TicketMessage> pendingMessages_; // messageId -> 当前已经拼接出的消息
    std::unordered_map<int, std::list<Requirement>> requestsByArtist_; // // artistId -> 该艺人的所有未匹配需求

    // 题目提供的输出函数
    void process_data(int value) {
        std::cout << value << '\n';
    }

    void tryMatch(int messageId) {
        auto messageIt = pendingMessages_.find(messageId);
        if (messageIt == pendingMessages_.end()) {
            return;
        }

        TicketMessage& message = messageIt->second;
        if (!message.complete()) { // 三个数据包还没有全部到达
            return;
        }
        int artistId = *message.artistId;
        int ticketPrice = *message.ticketPrice;
        int availableSeats = *message.availableSeats;

        auto artistIt = requestsByArtist_.find(artistId);
        if (artistIt == requestsByArtist_.end()) {
            pendingMessages_.erase(messageIt);
            return;
        }

        std::list<Requirement>& requirements = artistIt->second;
        for (auto reqIt = requirements.begin(); reqIt != requirements.end(); ++reqIt) {
            bool priceMatches = ticketPrice <= reqIt->maxPrice;
            bool seatsMatch = availableSeats >= reqIt->requiredSeats;

            if (priceMatches && seatsMatch) {
                process_data(messageId);
                process_data(reqIt->requiredSeats);

                // 这个需求只能匹配一次
                requirements.erase(reqIt);

                // 这个票源消息只能匹配一次
                pendingMessages_.erase(messageIt);

                // 该 artist 已经没有需求了，删除空 list
                if (requirements.empty()) {
                    requestsByArtist_.erase(artistIt);
                }
                return;
            }
        }

        /*
         * 消息已经完整，但当前没有匹配需求。
         *
         * 如果未来不会再添加新需求，可以直接 erase。
         * 如果未来可能继续添加需求，则应暂时保留。
         */
        pendingMessages_.erase(messageIt);
    }

public:
    void addRequirement(int artistId, int maxPrice, int requiredSeats) {
        requestsByArtist_[artistId].push_back({ maxPrice, requiredSeats });
    }

    void receiveArtist(int messageId, int artistId) {
        pendingMessages_[messageId].artistId = artistId;
        tryMatch(messageId);
    }

    void receiveTicketPrice(int messageId, int ticketPrice) {
        pendingMessages_[messageId].ticketPrice = ticketPrice;
        tryMatch(messageId);
    }

    void receiveAvailableSeats(int messageId, int availableSeats) {
        pendingMessages_[messageId].availableSeats = availableSeats;
        tryMatch(messageId);
    }
};

// 几百万个需求？std::unordered_map< int, std::multimap<int, Requirement>> requestsByArtistAndPrice_; 
// 按照价格建立map artistId -> maxPrice -> Requirement  lower_bound
// 不完整消息一直收不齐怎么办？add timestamp, delete 
