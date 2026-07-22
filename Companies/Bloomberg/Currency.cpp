struct Edge {
    std::string to;
    double rate;
};

std::unordered_map<std::string, std::vector<Edge>> graph_;

void addRate(const std::string& from, const std::string& to, double rate)
{
    graph_[from].push_back({to, rate});
    graph_[to].push_back({from, 1.0 / rate});
}

double bestRate(const std::string& source, const std::string& target) 
{
    if (!graph_.contains(source) || !graph_.contains(target)) {
        return -1.0;
    }

    if (source == target) {
        return 1.0;
    }

    std::unordered_set<std::string> visited;
    return dfs(source, target, 1.0, visited);
}

double dfs(const std::string& current, const std::string& target, double currentRate, std::unordered_set<std::string>& visited) const 
{
    if (current == target) {
        return currentRate;
    }

    visited.insert(current);

    double best = -1.0;

    auto it = graph_.find(current);
    if (it != graph_.end()) {
        for (const Edge& edge : it->second) {
            if (visited.contains(edge.to)) {
                continue;
            }

            double candidate = dfs(
                edge.to,
                target,
                currentRate * edge.rate,
                visited
            );

            best = std::max(best, candidate);
        }
    }

    // Backtracking: current may appear in another candidate path.
    visited.erase(current);

    return best;
}
