public List<String> mostVisitedPattern(String[] username, int[] timestamp, String[] website) {
     // Map to store each user's visit history
        Map<String, List<Visit>> userVisitsMap = new HashMap<>();
        int n = username.length;

        // Group visits by user
        for (int i = 0; i < n; i++) {
            String user = username[i];
            int time = timestamp[i];
            String site = website[i];
            userVisitsMap.computeIfAbsent(user, k -> new ArrayList<>())
                         .add(new Visit(user, time, site));
        }

        // Map to count occurrences of each 3-sequence pattern
        Map<String, Integer> patternCount = new HashMap<>();

        // Process each user's visit history
        for (List<Visit> visits : userVisitsMap.values()) {
            int visitCount = visits.size();
            Set<String> uniquePatterns = new HashSet<>();

            // Only process users with at least 3 visits
            if (visitCount > 2) {
                // Sort visits by timestamp
                Collections.sort(visits, (a, b) -> a.timestamp - b.timestamp);

                // Generate all possible 3-sequences in chronological order
                for (int i = 0; i < visitCount - 2; i++) {
                    for (int j = i + 1; j < visitCount - 1; j++) {
                        for (int k = j + 1; k < visitCount; k++) {
                            // Create pattern string with comma separator
                            String pattern = visits.get(i).website + "," +
                                           visits.get(j).website + "," +
                                           visits.get(k).website;
                            uniquePatterns.add(pattern);
                        }
                    }
                }
            }

            // Count each unique pattern for this user
            for (String pattern : uniquePatterns) {
                patternCount.put(pattern, patternCount.getOrDefault(pattern, 0) + 1);
            }
        }

        // Find the most frequent pattern (lexicographically smallest if tie)
        int maxCount = 0;
        String mostFrequentPattern = "";

        for (Map.Entry<String, Integer> entry : patternCount.entrySet()) {
            String currentPattern = entry.getKey();
            int currentCount = entry.getValue();

            // Update if higher count or same count but lexicographically smaller
            if (currentCount > maxCount ||
                (currentCount == maxCount && currentPattern.compareTo(mostFrequentPattern) < 0)) {
                maxCount = currentCount;
                mostFrequentPattern = currentPattern;
            }
        }

        // Split the pattern string and return as list
        return Arrays.asList(mostFrequentPattern.split(","));
    }
}

/**
 * Class to represent a user's visit to a website at a specific time
 */
class Visit {
    String user;
    int timestamp;
    String website;

    Visit(String user, int timestamp, String website) {
        this.user = user;
        this.timestamp = timestamp;
        this.website = website;
    }
