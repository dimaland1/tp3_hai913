package org.example.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Product;
import org.example.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
@Profile("!test")
public class CLIRunner implements CommandLineRunner {
    private static final Logger logger = LogManager.getLogger(CLIRunner.class);

    private final CLI cli;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;

    public CLIRunner(CLI cli) {
        this.cli = cli;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    @Override
    public void run(String... args) {
        logger.info("Application started. Initializing the CLI Runner.");
        System.out.println("Welcome to the Product Management System!");
        loginMenu();
    }

    private void loginMenu() {
        while (true) {
            if (cli.getCurrentUser() != null) {
                logger.info("User {} is already logged in. Redirecting to main menu.", cli.getCurrentUser().getName());
                mainMenu();
                continue;
            }

            System.out.println("\n=== Login Menu ===");
            System.out.println("1. Create new user");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        logger.debug("User chose to create a new user.");
                        createUser();
                        break;
                    case 2:
                        logger.debug("User chose to log in.");
                        login();
                        break;
                    case 3:
                        logger.info("Exiting the application.");
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        logger.warn("Invalid option selected: {}", choice);
                        System.out.println("Invalid option! Please try again.");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid input, expected a number. Error: {}", e.getMessage());
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void mainMenu() {
        boolean inMainMenu = true;
        while (inMainMenu && cli.getCurrentUser() != null) {
            System.out.println("\n=== Product Management ===");
            System.out.println("Welcome, " + cli.getCurrentUser().getName() + "!");
            System.out.println("1. Display all products");
            System.out.println("2. Find product by ID");
            System.out.println("3. Add new product");
            System.out.println("4. Update product");
            System.out.println("5. Delete product");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        logger.debug("User {} selected: Display all products.", cli.getCurrentUser().getName());
                        displayAllProducts();
                        break;
                    case 2:
                        logger.debug("User {} selected: Find product by ID.", cli.getCurrentUser().getName());
                        findProductById();
                        break;
                    case 3:
                        logger.debug("User {} selected: Add new product.", cli.getCurrentUser().getName());
                        addProduct();
                        break;
                    case 4:
                        logger.debug("User {} selected: Update product.", cli.getCurrentUser().getName());
                        updateProduct();
                        break;
                    case 5:
                        logger.debug("User {} selected: Delete product.", cli.getCurrentUser().getName());
                        deleteProduct();
                        break;
                    case 6:
                        logger.info("User {} is logging out.", cli.getCurrentUser().getName());
                        logout();
                        inMainMenu = false;
                        break;
                    default:
                        logger.warn("Invalid option selected by user: {}", choice);
                        System.out.println("Invalid option! Please try again.");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid input in main menu. Error: {}", e.getMessage());
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                logger.error("Unexpected error in main menu: {}", e.getMessage());
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createUser() {
        try {
            System.out.println("\n=== Create New User ===");
            System.out.print("Enter ID: ");
            String id = scanner.nextLine();

            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            System.out.print("Enter age: ");
            int age = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User user = new User(id, name, age, email, password);
            cli.createUser(user);
            logger.info("New user created: {}", user);
            System.out.println("User created successfully!");
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private void login() {
        try {
            System.out.println("\n=== Login ===");
            System.out.print("Enter user ID: ");
            String id = scanner.nextLine();
            cli.login(id);
            logger.info("User {} logged in successfully.", cli.getCurrentUser().getName());
            System.out.println("Welcome back, " + cli.getCurrentUser().getName() + "!");
        } catch (RuntimeException e) {
            logger.warn("Login failed. Reason: {}", e.getMessage());
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void displayAllProducts() {
        try {
            System.out.println("\n=== All Products ===");
            List<Product> products = cli.getAllProducts();
            if (products.isEmpty()) {
                logger.info("No products found.");
                System.out.println("No products found.");
            } else {
                logger.info("{} products found.", products.size());
                for (Product product : products) {
                    displayProduct(product);
                }
            }
        } catch (Exception e) {
            logger.error("Error displaying products: {}", e.getMessage());
            System.out.println("Error displaying products: " + e.getMessage());
        }
    }

    private void findProductById() {
        try {
            System.out.print("Enter product ID: ");
            String id = scanner.nextLine();
            Product product = cli.getProductById(id);
            logger.info("Product found: {}", product);
            System.out.println("\n=== Product Found ===");
            displayProduct(product);
        } catch (Exception e) {
            logger.error("Error finding product by ID: {}", e.getMessage());
            System.out.println("Error finding product: " + e.getMessage());
        }
    }

    private void addProduct() {
        try {
            System.out.println("\n=== Add New Product ===");
            System.out.print("Enter product ID: ");
            String id = scanner.nextLine();

            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            System.out.print("Enter price: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter expiration date (DD-MM-YYYY, e.g. 31-12-2024): ");
            LocalDate expirationDate = LocalDate.parse(scanner.nextLine(), dateFormatter);

            Product product = new Product(id, name, price, expirationDate);
            cli.createProduct(product);
            logger.info("New product added: {}", product);
            System.out.println("Product added successfully!");
        } catch (Exception e) {
            logger.error("Error adding product: {}", e.getMessage());
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private void updateProduct() {
        try {
            System.out.println("\n=== Update Product ===");
            System.out.print("Enter product ID to update: ");
            String id = scanner.nextLine();

            // First fetch the existing product
            Product existingProduct = cli.getProductById(id);
            logger.debug("Updating product: {}", existingProduct);

            System.out.println("Current product details:");
            displayProduct(existingProduct);

            System.out.println("\nEnter new details (press Enter to keep current value):");

            System.out.print("Enter new name (current: " + existingProduct.getName() + "): ");
            String name = scanner.nextLine();
            if (name.isEmpty()) {
                name = existingProduct.getName();
            }

            System.out.print("Enter new price (current: " + existingProduct.getPrice() + "): ");
            String priceStr = scanner.nextLine();
            double price = priceStr.isEmpty() ? existingProduct.getPrice() : Double.parseDouble(priceStr);

            System.out.print("Enter new expiration date (DD-MM-YYYY, current: " +
                    existingProduct.getExpirationDate().format(dateFormatter) + "): ");
            String dateStr = scanner.nextLine();
            LocalDate expirationDate = dateStr.isEmpty() ?
                    existingProduct.getExpirationDate() :
                    LocalDate.parse(dateStr, dateFormatter);

            Product updatedProduct = new Product(id, name, price, expirationDate);
            cli.updateProduct(id, updatedProduct);
            logger.info("Product updated successfully: {}", updatedProduct);
            System.out.println("Product updated successfully!");

            System.out.println("\nUpdated product details:");
            displayProduct(updatedProduct);

        } catch (Exception e) {
            logger.error("Error updating product: {}", e.getMessage());
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        try {
            System.out.println("\n=== Delete Product ===");
            System.out.print("Enter product ID to delete: ");
            String id = scanner.nextLine();

            // First fetch and display the product to confirm
            Product product = cli.getProductById(id);
            System.out.println("\nProduct to delete:");
            displayProduct(product);

            System.out.print("Are you sure you want to delete this product? (y/n): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("y")) {
                cli.deleteProduct(id);
                logger.info("Product deleted: {}", product);
                System.out.println("Product deleted successfully!");
            } else {
                logger.debug("Product deletion cancelled by user for ID: {}", id);
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            logger.error("Error deleting product: {}", e.getMessage());
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    private void logout() {
        logger.info("User {} logged out.", cli.getCurrentUser().getName());
        cli.clearCurrentUser();
        System.out.println("Logged out successfully!");
    }

    private void displayProduct(Product product) {
        System.out.println("-------------------");
        System.out.println("ID: " + product.getId());
        System.out.println("Name: " + product.getName());
        System.out.println("Price: $" + String.format("%.2f", product.getPrice()));
        System.out.println("Expiration Date: " + product.getExpirationDate().format(dateFormatter));
        System.out.println("-------------------");
    }
}
