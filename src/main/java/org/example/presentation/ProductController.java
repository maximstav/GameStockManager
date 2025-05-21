package org.example.presentation;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import org.example.bussinessLayer.ProductService;
import org.example.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class ProductController {

    @FXML private TextField nameField;
    @FXML private TextField stockField;
    @FXML private TextField priceField;
    @FXML private Button addProductButton;
    @FXML private Button updateProductButton;
    @FXML private FlowPane productFlowPane;

    private final ProductService productService = new ProductService();
    private Product selectedProduct;

    private final Random random = new Random();
    private final String[] imagePaths = {
            "/images/GotG.jpg", "/images/DOOM,_The_Dark_Ages_Game_Cover.jpeg", "/images/Cyberpunk2077.png",
            "/images/GTA6.jpg", "/images/Mario_Kart_World_Cover_Artwork.png", "/images/zelda.jpg"
            // Ensure these paths exist in src/main/resources
    };

    @FXML
    public void initialize() {
        loadProducts();
        addProductButton.setOnAction(e -> handleAddProduct());
        updateProductButton.setOnAction(e -> handleUpdateProduct());
        //deleteProductButton.setOnAction(e -> handleDeleteProduct());
    }

    private void loadProducts() {
        productFlowPane.getChildren().clear();
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            VBox card = createProductCard(product);
            productFlowPane.getChildren().add(card);
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(8);
        card.setPrefWidth(180);
        card.setPrefHeight(260); // sau o valoare mai mare dacă e nevoie
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("product-card");

        ImageView imageView = new ImageView(getRandomProductImage());
        imageView.setFitWidth(160);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label priceLabel = new Label("Price: $" + product.getPrice());
        priceLabel.setStyle("-fx-text-fill: #aaaaaa;");

        Label stockLabel = new Label("Stock: " + product.getQuantity());
        stockLabel.setStyle("-fx-text-fill: #aaaaaa;");

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);

        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");

        updateBtn.setOnAction(e -> populateForm(product));
        deleteBtn.setOnAction(e -> {
            productService.deleteProduct(product.getId());
            loadProducts();
            clearForm();
        });

        buttonBox.getChildren().addAll(updateBtn, deleteBtn);

        card.getChildren().addAll(imageView, nameLabel, priceLabel, stockLabel, buttonBox);
        return card;
    }

    private void populateForm(Product product) {
        selectedProduct = product;
        nameField.setText(product.getName());
        stockField.setText(String.valueOf(product.getQuantity()));
        priceField.setText(product.getPrice().toString());
    }

    private void handleAddProduct() {
        String name = nameField.getText().trim();
        String quantityStr = stockField.getText().trim();
        String priceStr = priceField.getText().trim();

        if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            BigDecimal price = new BigDecimal(priceStr);

            Product newProduct = new Product(0, name, quantity, price);
            productService.addProduct(newProduct);
            loadProducts();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Stock must be integer, price must be a number.");
        }
    }

    private void handleUpdateProduct() {
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Select a product to update.");
            return;
        }

        try {
            selectedProduct.setName(nameField.getText().trim());
            selectedProduct.setQuantity(Integer.parseInt(stockField.getText().trim()));
            selectedProduct.setPrice(new BigDecimal(priceField.getText().trim()));

            productService.updateProduct(selectedProduct);
            loadProducts();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid number format.");
        }
    }

    private void handleDeleteProduct() {
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Select a product to delete.");
            return;
        }

        productService.deleteProduct(selectedProduct.getId());
        loadProducts();
        clearForm();
    }

    private void clearForm() {
        nameField.clear();
        stockField.clear();
        priceField.clear();
        selectedProduct = null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Image getRandomProductImage() {
        String path = imagePaths[random.nextInt(imagePaths.length)];
        return new Image(getClass().getResourceAsStream(path));
    }
}
