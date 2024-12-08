package org.example.service;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserContextService userContextService;

    @Autowired
    public UserService(UserRepository userRepository, UserContextService userContextService) {
        this.userRepository = userRepository;
        this.userContextService = userContextService;
    }

    public User createUser(User user) {
        try {
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger("UserProfile");
            String userId = java.util.Optional.ofNullable(org.example.context.UserContext.getCurrentUser())
                    .map(u -> u.getId())
                    .orElse("unknown");
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode logData = mapper.createObjectNode()
                    .put("method", "createUser")
                    .put("type", "CREATE")
                    .put("userId", userId)
                    .put("timestamp", System.currentTimeMillis());
            logger.info(mapper.writeValueAsString(logData));
        } catch (Exception e) {
            System.err.println("Error logging operation: " + e.getMessage());
        }
        ;
        return userRepository.save(user);
    }

    public User getUserById(String id) throws RuntimeException {
        try {
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger("UserProfile");
            String userId = java.util.Optional.ofNullable(org.example.context.UserContext.getCurrentUser())
                    .map(u -> u.getId())
                    .orElse("unknown");
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode logData = mapper.createObjectNode()
                    .put("method", "getUserById")
                    .put("type", "READ")
                    .put("userId", userId)
                    .put("timestamp", System.currentTimeMillis());
            logger.info(mapper.writeValueAsString(logData));
        } catch (Exception e) {
            System.err.println("Error logging operation: " + e.getMessage());
        }
        ;
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User getCurrentUser() {
        try {
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger("UserProfile");
            String userId = java.util.Optional.ofNullable(org.example.context.UserContext.getCurrentUser())
                    .map(u -> u.getId())
                    .orElse("unknown");
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode logData = mapper.createObjectNode()
                    .put("method", "getCurrentUser")
                    .put("type", "READ")
                    .put("userId", userId)
                    .put("timestamp", System.currentTimeMillis());
            logger.info(mapper.writeValueAsString(logData));
        } catch (Exception e) {
            System.err.println("Error logging operation: " + e.getMessage());
        }
        ;
        return userContextService.getCurrentUser();
    }
}