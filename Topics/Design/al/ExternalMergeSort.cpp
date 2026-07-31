std::vector<long long> sort(const std::vector<long long>& values, std::size_t runCapacity) {
    vector<vector<long long>> runs;
    for (int start = 0; start < values.size(); start += runCapacity) {
        int end = min(start+runCapacity, values.size());
        vector<long long> run(values.begin()+start, values.begin()+end);
        std::sort(run.begin(), run.end());
        runs.push_back(std::move(run));
    }

    using heapEntry = std::tuple<long long, int, int>;
    priority_queue<heapEntry, vector<heapEntry>, greater<heapEntry>> pq;

    for (int index = 0; index < runs.size(); index++) {
        pq.emplace(runs[index][0], index, 0);
    }

    vector<long long> re;
    re.reserve(values.size());
    while (!pq.empty()) {
        auto [value, index, po] = pq.top();
        pq.pop();

        re.push_back(value);
        if (po+1 < runs[index].size()) {
            pq.emplace(runs[index][po+1], index, po+1);
        }
    }
    return re;
}
