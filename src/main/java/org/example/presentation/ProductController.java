package org.example.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.bussinessLayer.ProductService;
import org.example.model.Product;

import java.math.BigDecimal;

public class ProductController {

    @FXML private TextField nameField;
    @FXML private TextField stockField;
    @FXML private TextField priceField;
    @FXML private Button addProductButton;
    @FXML private Button updateProductButton;
    @FXML private Button deleteProductButton;
    @FXML private TableView<Product> productTable;

    private final ProductService productService = new ProductService();
    private Product selectedProduct;

    @FXML
    public void initialize() {
        setupTable();
        loadProducts();
        setupListeners();

        addProductButton.setOnAction(e -> handleAddProduct());
        updateProductButton.setOnAction(e -> handleUpdateProduct());
        deleteProductButton.setOnAction(e -> handleDeleteProduct());
    }

    private void setupTable() {
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Product, Integer> qtyCol = new TableColumn<>("Stock");
        qtyCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        TableColumn<Product, BigDecimal> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getBigDecimalPrice()));

        productTable.getColumns().setAll(idCol, nameCol, qtyCol, priceCol);
    }

    private void loadProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList(productService.getAllProducts());
        productTable.setItems(products);
    }

    private void setupListeners() {
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProduct = newSelection;
                nameField.setText(selectedProduct.getName());
                stockField.setText(String.valueOf(selectedProduct.getQuantity()));
                priceField.setText(selectedProduct.getPrice().toString());
            }
        });
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
        productTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
