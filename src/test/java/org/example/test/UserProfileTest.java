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
public class UserProfileTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserContextService userContextService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.getDb().drop();
    }

    @Test
    @DisplayName("Test Reader Profile Generation")
    void testReaderProfile() {
        // Créer utilisateur lecteur
        User readerUser = new User("reader1", "Reader", 25, "reader@test.com", "pass");
        readerUser = userService.createUser(readerUser);
        userContextService.setCurrentUser(readerUser);

        // Simuler comportement lecteur (80% lectures, 20% écritures)
        for (int i = 0; i < 20; i++) {
            productService.getAllProducts();
            if (i % 5 == 0) {
                Product product = new Product(
                        "reader_prod" + i,
                        "Reader Product " + i,
                        50.0,
                        LocalDate.now()
                );
                productService.createProduct(product);
            }
        }

        // TODO: Ajouter vérifications selon votre implémentation du profilage
    }

    @Test
    @DisplayName("Test Writer Profile Generation")
    void testWriterProfile() {
        // Créer utilisateur écrivain
        User writerUser = new User("writer1", "Writer", 30, "writer@test.com", "pass");
        writerUser = userService.createUser(writerUser);
        userContextService.setCurrentUser(writerUser);

        // Simuler comportement écrivain (80% écritures, 20% lectures)
        for (int i = 0; i < 20; i++) {
            if (i % 5 == 0) {
                productService.getAllProducts();
            }
            Product product = new Product(
                    "writer_prod" + i,
                    "Writer Product " + i,
                    75.0,
                    LocalDate.now()
            );
            productService.createProduct(product);
        }

        // TODO: Ajouter vérifications selon votre implémentation du profilage
    }

    @Test
    @DisplayName("Test Premium Profile Generation")
    void testPremiumProfile() {
        // Créer utilisateur premium
        User premiumUser = new User("premium1", "Premium", 35, "premium@test.com", "pass");
        premiumUser = userService.createUser(premiumUser);
        userContextService.setCurrentUser(premiumUser);

        // Simuler comportement premium (produits chers)
        for (int i = 0; i < 15; i++) {
            Product product = new Product(
                    "premium_prod" + i,
                    "Premium Product " + i,
                    1500.0 + (i * 100),
                    LocalDate.now()
            );
            productService.createProduct(product);
            productService.getAllProducts();
        }

        // TODO: Ajouter vérifications selon votre implémentation du profilage
    }
}