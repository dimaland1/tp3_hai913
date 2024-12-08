package org.example.test;

import org.example.model.Product;
import org.example.model.User;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.example.service.UserContextService;
import org.example.test.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TestScenarios {

    private static final Logger logger = LoggerFactory.getLogger(TestScenarios.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserContextService userContextService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Nettoyer la base de données
        mongoTemplate.getDb().drop();

        // Créer un utilisateur de test
        testUser = new User("test1", "Test User", 25, "test@example.com", "password");
        testUser = userService.createUser(testUser);
        userContextService.setCurrentUser(testUser);
    }

    @Test
    @DisplayName("Test User Creation and Retrieval")
    void testUserCreationAndRetrieval() {
        // Test création utilisateur
        User newUser = new User("user1", "John Doe", 30, "john@example.com", "password123");
        User savedUser = userService.createUser(newUser);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(newUser.getName(), savedUser.getName());
        assertEquals(newUser.getEmail(), savedUser.getEmail());

        // Test récupération utilisateur
        User retrievedUser = userService.getUserById(savedUser.getId());
        assertNotNull(retrievedUser);
        assertEquals(savedUser.getName(), retrievedUser.getName());
        assertEquals(savedUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    @DisplayName("Test Product CRUD Operations")
    void testProductCRUD() {
        // Test Create
        Product product = new Product(
                "prod1",
                "Test Product",
                99.99,
                LocalDate.now().plusDays(30)
        );
        Product savedProduct = productService.createProduct(product);
        assertNotNull(savedProduct.getId());
        assertEquals(product.getName(), savedProduct.getName());
        logger.info("Created product: {}", savedProduct);

        // Test Read
        Product retrievedProduct = productService.getProductById(savedProduct.getId());
        assertNotNull(retrievedProduct);
        assertEquals(savedProduct.getName(), retrievedProduct.getName());
        assertEquals(savedProduct.getPrice(), retrievedProduct.getPrice());
        logger.info("Retrieved product: {}", retrievedProduct);

        // Test Read All
        List<Product> allProducts = productService.getAllProducts();
        assertFalse(allProducts.isEmpty());
        assertTrue(allProducts.contains(retrievedProduct));
        logger.info("All products: {}", allProducts);

        // Test Update
        retrievedProduct.setPrice(129.99);
        Product updatedProduct = productService.updateProduct(retrievedProduct.getId(), retrievedProduct);
        assertEquals(129.99, updatedProduct.getPrice());
        logger.info("Updated product: {}", updatedProduct);

        // Test Delete
        productService.deleteProduct(updatedProduct.getId());
        assertThrows(RuntimeException.class, () ->
                productService.getProductById(updatedProduct.getId())
        );
        logger.info("Deleted product with ID: {}", updatedProduct.getId());
    }

    @Test
    @DisplayName("Test Product Exception Cases")
    void testProductExceptions() {
        // Test produit non existant
        assertThrows(RuntimeException.class, () ->
                productService.getProductById("nonexistent")
        );
        logger.info("Handled exception for non-existent product retrieval");

        // Test création produit avec ID existant
        Product product = new Product(
                "prod1",
                "Test Product",
                99.99,
                LocalDate.now().plusDays(30)
        );
        productService.createProduct(product);

        assertThrows(RuntimeException.class, () ->
                productService.createProduct(product)
        );
        logger.info("Handled exception for creating product with existing ID");

        // Test mise à jour produit non existant
        Product nonExistentProduct = new Product(
                "nonexistent",
                "Non Existent Product",
                149.99,
                LocalDate.now().plusDays(30)
        );
        assertThrows(RuntimeException.class, () ->
                productService.updateProduct("nonexistent", nonExistentProduct)
        );
        logger.info("Handled exception for updating non-existent product");

        // Test suppression produit non existant
        assertThrows(RuntimeException.class, () ->
                productService.deleteProduct("nonexistent")
        );
        logger.info("Handled exception for deleting non-existent product");
    }
}