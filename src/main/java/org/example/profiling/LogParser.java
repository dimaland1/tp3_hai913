package org.example.profiling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class LogParser {
    private static final double PREMIUM_PRICE_THRESHOLD = 1000.0;
    private static final double READER_THRESHOLD = 0.7;
    private static final double WRITER_THRESHOLD = 0.3;
    private final ObjectMapper mapper = new ObjectMapper();

    public List<LPS> parseLogs(String logFilePath) throws Exception {
        List<LPS> profiles = new ArrayList<>();
        Map<String, UserStats> userStatsMap = new HashMap<>();

        // Lire et parser chaque ligne du fichier de log
        List<String> logLines = Files.readAllLines(Paths.get(logFilePath));
        for (String line : logLines) {
            if (line.contains("UserProfile")) {
                JsonNode logNode = mapper.readTree(line.substring(line.indexOf('{')));
                processLogEntry(logNode, userStatsMap);
            }
        }

        // Convertir les statistiques en profils LPS
        for (Map.Entry<String, UserStats> entry : userStatsMap.entrySet()) {
            profiles.add(createProfileFromStats(entry.getValue()));
        }

        // Sauvegarder les profils
        saveProfiles(profiles);

        return profiles;
    }

    private void processLogEntry(JsonNode logNode, Map<String, UserStats> userStatsMap) {
        String userId = logNode.get("userId").asText();
        String methodType = logNode.get("type").asText();
        String method = logNode.get("method").asText();
        long timestamp = logNode.get("timestamp").asLong();

        UserStats stats = userStatsMap.computeIfAbsent(userId, k -> new UserStats(userId));
        stats.updateStats(methodType, method, timestamp);
    }

    private LPS createProfileFromStats(UserStats stats) {
        String userType = determineUserType(stats);

        return new LPS.Builder()
                .withTimestamp(String.valueOf(System.currentTimeMillis()))
                .withEvent("USER_PROFILING")
                .withUserId(stats.userId)
                .withUserType(userType)
                .withOperationCounts(stats.readCount, stats.writeCount)
                .withAvgProductPrice(stats.getAverageProductPrice())
                .build();
    }

    private String determineUserType(UserStats stats) {
        double totalOps = stats.readCount + stats.writeCount;
        if (totalOps == 0) return "INACTIVE";

        double readRatio = stats.readCount / totalOps;

        if (stats.getAverageProductPrice() > PREMIUM_PRICE_THRESHOLD) {
            return "PREMIUM";
        } else if (readRatio > READER_THRESHOLD) {
            return "READER";
        } else if (readRatio < WRITER_THRESHOLD) {
            return "WRITER";
        } else {
            return "BALANCED";
        }
    }

    private void saveProfiles(List<LPS> profiles) throws Exception {
        File profileDir = new File("profiles");
        profileDir.mkdirs();

        for (LPS profile : profiles) {
            String filename = String.format("profiles/profile_%s.json", profile.getUserInfo().getUserId());
            mapper.writeValue(new File(filename), profile);
        }
    }

    private static class UserStats {
        String userId;
        int readCount;
        int writeCount;
        double totalProductPrice;
        int productCount;
        List<Long> timestamps;

        UserStats(String userId) {
            this.userId = userId;
            this.readCount = 0;
            this.writeCount = 0;
            this.totalProductPrice = 0;
            this.productCount = 0;
            this.timestamps = new ArrayList<>();
        }

        void updateStats(String methodType, String method, long timestamp) {
            timestamps.add(timestamp);

            if ("READ".equals(methodType)) {
                readCount++;
            } else {
                writeCount++;
            }
        }

        double getAverageProductPrice() {
            return productCount > 0 ? totalProductPrice / productCount : 0;
        }
    }
}