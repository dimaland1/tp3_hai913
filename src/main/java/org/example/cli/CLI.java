package org.example.cli;

import org.example.model.Product;
import org.example.model.User;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.example.service.UserContextService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CLI {
    private final ProductService productService;
    private final UserService userService;
    private final UserContextService userContextService;
    private User currentUser;

    public CLI(ProductService productService,
               UserService userService,
               UserContextService userContextService) {
        this.productService = productService;
        this.userService = userService;
        this.userContextService = userContextService;
    }

    public void createUser(User user) {
        User createdUser = userService.createUser(user);
        setCurrentUser(createdUser);
    }

    public void login(String userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            setCurrentUser(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        userContextService.setCurrentUser(user);
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void clearCurrentUser() {
        this.currentUser = null;
        userContextService.clearCurrentUser();
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public Product getProductById(String id) {
        return productService.getProductById(id);
    }

    public Product createProduct(Product product) {
        return productService.createProduct(product);
    }

    public Product updateProduct(String id, Product product) {
        return productService.updateProduct(id, product);
    }

    public void deleteProduct(String id) {
        productService.deleteProduct(id);
    }
}