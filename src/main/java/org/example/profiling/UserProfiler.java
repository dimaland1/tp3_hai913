package org.example.profiling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Product;
import org.example.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserProfiler {
    private static final ConcurrentHashMap<String, UserStats> userStats = new ConcurrentHashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String PROFILE_DIR = "profiles";
    private static final double PREMIUM_THRESHOLD = 1000.0;

    static {
        new File(PROFILE_DIR).mkdirs();
    }

    public static void logOperation(String operation, String type, Object... args) {
        User currentUser = getCurrentUser();
        if (currentUser == null) return;

        UserStats stats = userStats.computeIfAbsent(
                currentUser.getId(),
                k -> new UserStats(currentUser.getId())
        );

        if ("READ".equals(type)) {
            stats.readCount++;
            // Si l'opération implique un produit, vérifier son prix
            if (args.length > 0 && args[0] instanceof Product) {
                Product product = (Product) args[0];
                stats.addViewedProductPrice(product.getPrice());
            }
        } else if ("WRITE".equals(type)) {
            stats.writeCount++;
        }

        // Sauvegarder le profil après chaque opération
        saveProfile(currentUser.getId(), stats);
    }

    private static User getCurrentUser() {
        // Récupérer l'utilisateur depuis la session ou le contexte Spring
        // À adapter selon votre implémentation
        return null; // TODO: implémenter
    }

    private static void saveProfile(String userId, UserStats stats) {
        try {
            Path profilePath = Paths.get(PROFILE_DIR, "profile-" + userId + ".json");
            Map<String, Object> profile = new HashMap<>();
            profile.put("userId", userId);
            profile.put("readCount", stats.readCount);
            profile.put("writeCount", stats.writeCount);
            profile.put("averageProductPrice", stats.getAverageProductPrice());
            profile.put("profileType", stats.determineProfileType());
            profile.put("isPremium", stats.isPremium());

            Files.writeString(profilePath, mapper.writeValueAsString(profile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class UserStats {
        String userId;
        int readCount;
        int writeCount;
        double totalProductPrice;
        int productCount;

        UserStats(String userId) {
            this.userId = userId;
            this.readCount = 0;
            this.writeCount = 0;
            this.totalProductPrice = 0;
            this.productCount = 0;
        }

        void addViewedProductPrice(double price) {
            totalProductPrice += price;
            productCount++;
        }

        double getAverageProductPrice() {
            return productCount > 0 ? totalProductPrice / productCount : 0;
        }

        String determineProfileType() {
            if (readCount == 0 && writeCount == 0) return "INACTIVE";
            double readRatio = (double) readCount / (readCount + writeCount);
            if (readRatio > 0.7) return "READER";
            if (readRatio < 0.3) return "WRITER";
            return "BALANCED";
        }

        boolean isPremium() {
            return getAverageProductPrice() > PREMIUM_THRESHOLD;
        }
    }
}