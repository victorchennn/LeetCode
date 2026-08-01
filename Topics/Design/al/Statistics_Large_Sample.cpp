struct Statistics {
    double minimum;
    double maximum;
    double mean;
    double median;
    double mode;
    double standardDeviation;
};

Statistics calculateStatistics(const std::vector<long long>& frequency) {
    long long totalCount = 0;
    long double totalSum = 0;

    int minimum = -1;
    int maximum = -1;
    int mode = -1;
    long long maxFrequency = 0;

    for (int value = 0; value < static_cast<int>(frequency.size()); ++value) {
        long long count = frequency[value];
        if (count == 0) {
            continue;
        }

        if (minimum == -1) {
            minimum = value;
        }
        maximum = value;

        totalCount += count;
        totalSum += static_cast<long double>(value) * count;
      
        if (count > maxFrequency) {
            maxFrequency = count;
            mode = value;
        }
    }

    if (totalCount == 0) {
        throw std::invalid_argument("No data");
    }

    double mean = static_cast<double>(totalSum / totalCount);

    // 1-based positions of the two middle elements.
    long long leftPosition = (totalCount + 1) / 2;
    long long rightPosition = totalCount / 2 + 1;

    int leftMedian = -1;
    int rightMedian = -1;
    long long prefixCount = 0;

    long double squaredDifferenceSum = 0;

    for (int value = 0; value < static_cast<int>(frequency.size()); ++value) {
        long long count = frequency[value];

        if (count == 0) {
            continue;
        }

        prefixCount += count;
        if (leftMedian == -1 && prefixCount >= leftPosition) {
            leftMedian = value;
        }
        if (rightMedian == -1 && prefixCount >= rightPosition) {
            rightMedian = value;
        }

        long double difference = value - mean;
        squaredDifferenceSum += difference * difference * count;
    }

    double median = (leftMedian + rightMedian) / 2.0;

    // Population standard deviation.
    double variance = static_cast<double>(squaredDifferenceSum / totalCount);
    double standardDeviation = std::sqrt(variance);

    return {
        static_cast<double>(minimum),
        static_cast<double>(maximum),
        mean,
        median,
        static_cast<double>(mode),
        standardDeviation
    };
}
