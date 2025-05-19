package org.example;

import org.example.dataAccessLayer.BillDAO;
import org.example.model.*;
import org.example.bussinessLayer.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClientService clientService = new ClientService();
        ProductService productService = new ProductService();
        OrderService orderService = new OrderService();

        System.out.println("=== CLIENT MANAGEMENT ===");

        // --- Register a new client ---
        // Note: Role must be either 'admin' or 'client' according to DB schema
        Client newClient = new Client(0, "alice", "secure123", "client");
        clientService.registerClient(newClient);
        System.out.println("Client registered.");

        // --- Authenticate client ---
        Client authenticated = clientService.authenticate("alice", "secure123");
        if (authenticated != null) {
            System.out.println("Authentication successful: " + authenticated.getUsername());
        } else {
            System.out.println("Authentication failed - this could be due to database issues.");
        }

        // --- View all clients ---
        List<Client> clients = clientService.getAllClients();
        System.out.println("All clients:");
        if (clients.isEmpty()) {
            System.out.println(" - No clients found");
        } else {
            for (Client c : clients) {
                System.out.println(" - " + c.getUsername() + " (Role: " + c.getRole() + ")");
            }
        }

        System.out.println("\n=== PRODUCT MANAGEMENT ===");

        // --- Add a product using insert instead of update for a new product ---
        Product newProduct = new Product(0, "Laptop", 10, 999.99f);
        productService.addProduct(newProduct);

        // Add another product for testing
        Product secondProduct = new Product(0, "Mouse", 20, 29.99f);
        productService.addProduct(secondProduct);

        // --- View all products ---
        List<Product> products = productService.getAllProducts();
        System.out.println("Available products:");
        if (products.isEmpty()) {
            System.out.println(" - No products found");
        } else {
            for (Product p : products) {
                System.out.println(" - " + p.getName() + " (Stock: " + p.getQuantity() + ", Price: $" + p.getPrice() + ")");
            }
        }

        System.out.println("\n=== ORDER PROCESSING ===");

        // --- Only place an order if we have products and an authenticated client ---
        if (!products.isEmpty() && authenticated != null) {
            Product product = products.get(0); // take first product
            int quantity = 2;

            System.out.println("Placing order: " + quantity + "x " + product.getName() + " for user " + authenticated.getUsername());
            if (orderService.placeOrder(authenticated.getId(), product.getId(), quantity)) {
                System.out.println("Order placed successfully.");

                // Display the latest bill
                BillDAO billDAO = new BillDAO();
                List<Bill> bills = billDAO.findAll();
                if (!bills.isEmpty()) {
                    System.out.println("\n=== LATEST BILL ===");
                    Bill latestBill = bills.get(bills.size() - 1);
                    System.out.println("Bill ID: " + latestBill.id());
                    System.out.println("Created at: " + latestBill.createdAt());
                    System.out.println("Details:\n" + latestBill.billText());
                }
            } else {
                System.out.println("Order failed: insufficient stock or other error.");
            }
        } else {
            System.out.println("Cannot place order: " +
                    (products.isEmpty() ? "no products available" : "") +
                    (authenticated == null ? (products.isEmpty() ? " and " : "") + "no authenticated user" : ""));
        }
    }
}