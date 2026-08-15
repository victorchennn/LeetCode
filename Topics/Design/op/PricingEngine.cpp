// struct InstrumentProperties {
//     int mProperty1 = 0;
//     float mProperty2 = 0.0;
// };

// struct Parameters {
//     float mParam1 = 0.0;
//     float mParam2 = 0.0;
// };

// class ICalculator {
// public:
//     virtual float CalculatePrice(
//         InstrumentProperties properties,
//         Parameters parameters) = 0;
// };

class PricingEngine {
private:
    ICalculator& calculator_;
    mutex mutex_;

    unordered_map<int, InstrumentProperties> instruments_;
    Parameters parameters_;

public:
    PricingEngine(ICalculator& calculator) : calculator_(calculator) {}

    // Thread A / multiple threads may call this
    void OnInstrumentUpdate(int instrumentId, InstrumentProperties properties) {
        {
            lock_guard<mutex> lock(mutex_);
            instruments_[instrumentId] = properties;
        }
        Recalculate(instrumentId);
    }

    // Thread B may call this
    void OnParameterUpdate(Parameters parameters) {
        vector<pair<int, InstrumentProperties>> snapshot;
        {
            lock_guard<mutex> lock(mutex_);

            parameters_ = parameters;
            for (auto& [id, properties] : instruments_) {
                snapshot.push_back({id, properties});
            }
        }

        // Don't calculate while holding mutex
        for (auto& [id, properties] : snapshot) {
            float price = calculator_.CalculatePrice(properties, parameters);
            Publish(id, price);
        }
    }

private:
    void Recalculate(int instrumentId)
    {
        InstrumentProperties properties;
        Parameters parameters;

        {
            lock_guard<mutex> lock(mutex_);

            properties = instruments_[instrumentId];
            parameters = parameters_;
        }

        // expensive work outside lock
        float price = calculator_.CalculatePrice(properties, parameters);
        Publish(instrumentId, price);
    }

    void Publish(int instrumentId, float price)
    {
        // send price to downstream system
    }
};

// mutex解决：多个线程同时读写 memory  → data race
// 但 mutex 不自动解决：旧计算比新计算更晚完成  → stale result
struct InstrumentState {
    InstrumentProperties properties;
    uint64_t version = 0;
};

unordered_map<int, InstrumentState> instruments_;

Parameters parameters_;
uint64_t parameterVersion_ = 0;

if (instruments_[id].version != instrumentVersion || parameterVersion_ != parameterVersion) {
      return; // stale
}

// 一个更干净的设计：Single Pricing Thread： 不要让多个线程直接修改 pricing state。
// Thread 1: instrument updates ──┐
// Thread 2: parameter updates ───┼──> Queue -> Pricing Thread -> update state -> CalculatePrice/Publish
// Thread 3: other updates ───────┘

// 在 low-latency / trading 系统里经常希望：
// 让一个核心状态只有一个 owner/thread 修改，通过 queue 传递消息。

// state update 可以保持 single-writer，但 expensive calculation 可以 parallel。
