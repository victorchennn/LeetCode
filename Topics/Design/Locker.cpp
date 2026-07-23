enum class Size { SMALL, MEDIUM, LARGE };

class Locker {
  private:
      std::vector<Compartment*> compartments;
      std::unordered_map<std::string, AccessToken*> accessTokenMapping;
      std::mt19937 random;
  
      Compartment* getAvailableCompartment(Size size) {
          for (Compartment* c : compartments) {
              if (c->getSize() == size && !c->isOccupied()) {
                  return c;
              }
          }
          return nullptr;
      }
  
      AccessToken* generateAccessToken(Compartment* compartment) {
          std::uniform_int_distribution<int> dist(0, 999999);
          int randomNum = dist(random);
  
          std::stringstream ss;
          ss << std::setfill('0') << std::setw(6) << randomNum;
          std::string code = ss.str();
  
          auto expiration = std::chrono::system_clock::now() + std::chrono::hours(7 * 24);
          return new AccessToken(code, expiration, compartment);
      }
  
      void clearDeposit(AccessToken* accessToken) {
          Compartment* compartment = accessToken->getCompartment();
          compartment->markFree();
          accessTokenMapping.erase(accessToken->getCode());
          delete accessToken;
      }

public:
    Locker(const std::vector<Compartment*>& comps)
        : compartments(comps) {
        std::random_device rd;
        random.seed(rd());
    }

    ~Locker() {
        for (auto& pair : accessTokenMapping) {
            delete pair.second;
        }
    }

    std::string depositPackage(Size size) {
        Compartment* compartment = getAvailableCompartment(size);
        if (compartment == nullptr) {
            throw std::runtime_error("No available compartment of requested size");
        }

        compartment->open();
        compartment->markOccupied();
        AccessToken* accessToken = generateAccessToken(compartment);
        accessTokenMapping[accessToken->getCode()] = accessToken;

        return accessToken->getCode();
    }

    void pickup(const std::string& tokenCode) {
        if (tokenCode.empty()) {
            throw std::runtime_error("Invalid access token code");
        }

        auto it = accessTokenMapping.find(tokenCode);
        if (it == accessTokenMapping.end()) {
            throw std::runtime_error("Invalid access token code");
        }

        AccessToken* accessToken = it->second;
        if (accessToken->isExpired()) {
            throw std::runtime_error("Access token has expired");
        }

        Compartment* compartment = accessToken->getCompartment();
        compartment->open();
        clearDeposit(accessToken);
    }

    // void openExpiredCompartments() {
    //     for (const auto& pair : accessTokenMapping) {
    //         AccessToken* accessToken = pair.second;
    //         if (accessToken->isExpired()) {
    //             Compartment* compartment = accessToken->getCompartment();
    //             compartment->open();
    //         }
    //     }
    // }
};

class AccessToken {
    private:
        std::string code;
        std::chrono::system_clock::time_point expiration;
        Compartment* compartment;
    
    public:
        AccessToken(const std::string& code, std::chrono::system_clock::time_point expiration, Compartment* compartment)
            : code(code), expiration(expiration), compartment(compartment) {}
    
        bool isExpired() { return std::chrono::system_clock::now() >= expiration;}
        Compartment* getCompartment() { return compartment; }
        std::string getCode() const { return code;}
};

class Compartment {
    private:
        Size size;
        bool occupied;
    
    public:
        Compartment(Size size) : size(size), occupied(false) {}
    
        Size getSize() const { return size; }
        bool isOccupied() const { return occupied; }
        void markOccupied() { occupied = true; }
        void markFree() { occupied = false; }
        void open() {}
};


