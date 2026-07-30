vector<long long> minimumCosts(vector<int>& regular, vector<int>& express, int expressCost) {
    int n = regular.size();
    vector<long long> result(n);

    long long previousRegular = 0;
    long long previousExpress = LLONG_MAX / 4;

    for (int i = 0; i < n; ++i) {
        long long currentRegular = min(previousRegular, previousExpress) + regular[i];
        long long currentExpress = min(previousExpress, previousRegular + expressCost) + express[i];

        result[i] = min(currentRegular, currentExpress);

        previousRegular = currentRegular;
        previousExpress = currentExpress;
    }

    return result;
}

// route?
pair<long long, vector<int>> minimumCostWithPath(vector<int>& regular, vector<int>& express, int expressEntryCost) {
    int n = regular.size();
    const long long INF = LLONG_MAX / 4;

    vector<long long> regularDp(n + 1, INF);
    vector<long long> expressDp(n + 1, INF);

    vector<int> parentRegular(n + 1);
    vector<int> parentExpress(n + 1);

    regularDp[0] = 0;

    for (int i = 1; i <= n; ++i) {
        // 到达站点 i，并走 regular[i - 1]
        if (regularDp[i - 1] <= expressDp[i - 1]) {
            regularDp[i] = regularDp[i - 1] + regular[i - 1];
            parentRegular[i] = 0;
        } else {
            regularDp[i] = expressDp[i - 1] + regular[i - 1];
            parentRegular[i] = 1;
        }

        // 到达站点 i，并走 express[i - 1]
        if (regularDp[i - 1] + expressEntryCost <= expressDp[i - 1]) {
            expressDp[i] = regularDp[i - 1] + expressEntryCost + express[i - 1];
            parentExpress[i] = 0;
        } else {
            expressDp[i] = expressDp[i - 1] + express[i - 1];
            parentExpress[i] = 1;
        }
    }

    int lane;
    long long minCost;

    if (regularDp[n] <= expressDp[n]) {
        minCost = regularDp[n];
        lane = 0;
    } else {
        minCost = expressDp[n];
        lane = 1;
    }

    vector<int> path(n);

    for (int i = n; i >= 1; --i) {
        path[i - 1] = lane;

        if (lane == 0) {
            lane = parentRegular[i];
        } else {
            lane = parentExpress[i];
        }
    }

    return {minCost, path};
}
