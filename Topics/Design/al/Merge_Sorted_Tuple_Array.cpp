// 给定多个sorted tuple array, [{timestamp, price change}]，merge成一个一维的sorted tuple array，[{timestamp, current price}]

using Event = pair<int, int>; // {timestamp, priceChange}

vector<Event> mergePriceChanges(const vector<vector<Event>>& data) {
    using Node = tuple<int, int, int>; // {timestamp, arrayIndex, elementIndex}
    priority_queue<Node, vector<Node>, greater<Node>> pq;

    for (int i = 0; i < data.size(); ++i)
        if (!data[i].empty())
            pq.push({data[i][0].first, i, 0});

    vector<Event> result;
    int currentPrice = 0;

    while (!pq.empty()) {
        int timestamp = std::get<0>(pq.top());
        int totalChange = 0;

        while (!pq.empty() && get<0>(pq.top()) == timestamp) {
            auto [ts, arrayIndex, index] = pq.top();
            pq.pop();

            totalChange += data[arrayIndex][index].second;

            if (++index < data[arrayIndex].size())
                pq.push({data[arrayIndex][index].first, arrayIndex, index});
        }

        currentPrice += totalChange;
        result.push_back({timestamp, currentPrice});
    }

    return result;
}
