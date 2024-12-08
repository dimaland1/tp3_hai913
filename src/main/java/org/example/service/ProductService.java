package org.example.service;
import java.util.List;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Service class handling business logic for Product operations.
 * This class serves as an intermediary between the repository layer and the CLI.
 */
@Service
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * Constructor injection of ProductRepository.
     *
     * @param productRepository
     * 		MongoDB repository for Product entities
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        try {
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger("UserProfile");
            String userId = java.util.Optional.ofNullable(org.example.context.UserContext.getCurrentUser())
                .map(u -> u.getId())
                .orElse("unknown");
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode logData = mapper.createObjectNode()
                .put("method", "getAllProducts")
                .put("type", "READ")
                .put("userId", userId)
                .put("timestamp", System.currentTimeMillis());
            logger.info(mapper.writeValueAsString(logData));
        } catch (Exception e) {
            System.err.println("Error logging operation: " + e.getMessage());
        }
        ;
        return productRepository.findAll();
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id
     * 		The ID of the product to retrieve
     * @return The found product
     * @throws RuntimeException
     * 		if no product is found with the given ID
     */
    public Product getProductById(String id) {
        try {
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger("UserProfile");
            String userId = java.util.Optional.ofNullable(org.example.context.UserContext.getCurrentUser())
                .map(u -> u.getId())
                .orElse("unknown");
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode logData = mapper.createObjectNode()
                .put("method", "getProductById")
                .put("type", "READ")
                .put("userId", userId)
                .put("timestamp", System.currentTimeMillis());
            logger.info(mapper.writeValueAsString(logData));
        } catch (Exception e) {
            System.err.println("Error logging operation: " + e.getMessage());
        }
        ;
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    /**
     * Creates a new product in the database.
     *
     * @param product
     * 		The product to create
     * @return The created product
     * @throws RuntimeException
     * 		if a product with the same ID already exists
     */
    public Product createProduct(Product product) {
        try {
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger("UserProfile");
            String userId = java.util.Optional.ofNullable(org.example.context.UserContext.getCurrentUser())
                .map(u -> u.getId())
                .orElse("unknown");
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode logData = mapper.createObjectNode()
                .put("method", "createProduct")
                .put("type", "CREATE")
                .put("userId", userId)
                .put("timestamp", System.currentTimeMillis());
            logger.info(mapper.writeValueAsString(logData));
        } catch (Exception e) {
            System.err.println("Error logging operation: " + e.getMessage());
        }
        ;
        // Check if product already exists
        if (productRepository.existsById(product.getId())) {
            throw new RuntimeException("Product already exists with ID: " + product.getId());
        }
        return productRepository.save(product);
    }

    /**
     * Updates an existing product in the database.
     *
     * @param id
     * 		The ID of the product to update
     * @param product
     * 		The updated product information
     * @return The updated product
     * @throws RuntimeException
     * 		if no product is found with the given ID
     */
    public Product updateProduct(String id, Product product) {
        try {
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger("UserProfile");
            String userId = java.util.Optional.ofNullable(org.example.context.UserContext.getCurrentUser())
                .map(u -> u.getId())
                .orElse("unknown");
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode logData = mapper.createObjectNode()
                .put("method", "updateProduct")
                .put("type", "UPDATE")
                .put("userId", userId)
                .put("timestamp", System.currentTimeMillis());
            logger.info(mapper.writeValueAsString(logData));
        } catch (Exception e) {
            System.err.println("Error logging operation: " + e.getMessage());
        }
        ;
        // Verify product exists before updating
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        product.setId(id);
        return productRepository.save(product);
    }

    /**
     * Deletes a product from the database.
     *
     * @param id
     * 		The ID of the product to delete
     * @throws RuntimeException
     * 		if no product is found with the given ID
     */
    public void deleteProduct(String id) {
        try {
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger("UserProfile");
            String userId = java.util.Optional.ofNullable(org.example.context.UserContext.getCurrentUser())
                .map(u -> u.getId())
                .orElse("unknown");
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode logData = mapper.createObjectNode()
                .put("method", "deleteProduct")
                .put("type", "DELETE")
                .put("userId", userId)
                .put("timestamp", System.currentTimeMillis());
            logger.info(mapper.writeValueAsString(logData));
        } catch (Exception e) {
            System.err.println("Error logging operation: " + e.getMessage());
        }
        ;
        // Verify product exists before deleting
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }
}