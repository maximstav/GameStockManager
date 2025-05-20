package org.example.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.bussinessLayer.ClientService;
import org.example.bussinessLayer.OrderService;
import org.example.bussinessLayer.ProductService;
import org.example.dataAccessLayer.OrderDAO;
import org.example.model.Client;
import org.example.model.Order;
import org.example.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    @FXML private TableView<OrderView> orderTable;
    @FXML private TableColumn<OrderView, Integer> clientIdCol;
    @FXML private TableColumn<OrderView, String> clientNameCol;
    @FXML private TableColumn<OrderView, Integer> productIdCol;
    @FXML private TableColumn<OrderView, String> productNameCol;
    @FXML private TableColumn<OrderView, Integer> quantityCol;
    @FXML private TableColumn<OrderView, BigDecimal> priceCol;

    @FXML private ComboBox<Client> clientComboBox;
    @FXML private ComboBox<Product> productComboBox;
    @FXML private TextField quantityField;
    @FXML private Label statusLabel;

    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductService productService = new ProductService();
    private final ClientService clientService = new ClientService();
    private final OrderService orderService = new OrderService();

    @FXML
    public void initialize() {
        // Table setup
        clientIdCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientNameCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Load combo box data
        clientComboBox.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
        productComboBox.setItems(FXCollections.observableArrayList(productService.getAllProducts()));

        clientComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                setText(empty || client == null ? null : client.getUsername());
            }
        });
        clientComboBox.setButtonCell(clientComboBox.getCellFactory().call(null));

        productComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                setText(empty || product == null ? null : product.getName());
            }
        });
        productComboBox.setButtonCell(productComboBox.getCellFactory().call(null));

        loadOrders();
    }

    private void loadOrders() {
        List<Order> orders = orderDAO.findAll();
        List<OrderView> orderViews = new ArrayList<>();

        for (Order order : orders) {
            Client client = clientService.getClientById(order.getClient_id());
            Product product = productService.getProductById(order.getProduct_id());

            if (client != null && product != null) {
                BigDecimal totalPrice = product.getBigDecimalPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
                orderViews.add(new OrderView(
                        order.getId(),
                        client.getId(),
                        client.getUsername(),
                        product.getId(),
                        product.getName(),
                        order.getQuantity(),
                        totalPrice
                ));

            }
        }

        orderTable.setItems(FXCollections.observableArrayList(orderViews));
    }

    @FXML
    private void handlePlaceOrder() {
        Client client = clientComboBox.getValue();
        Product product = productComboBox.getValue();
        String qtyText = quantityField.getText();

        if (client == null || product == null || qtyText.isEmpty()) {
            statusLabel.setText("Please fill all fields.");
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyText);
            boolean success = orderService.placeOrder(client.getId(), product.getId(), quantity);
            if (success) {
                statusLabel.setText("Order placed successfully!");
                quantityField.clear();
                loadOrders();
            } else {
                statusLabel.setText("Not enough stock.");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid quantity.");
        }
    }

    @FXML
    private void handleDeleteOrder() {
        OrderView selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean deleted = orderService.deleteOrder(selected.getOrderId());
            if (deleted) {
                statusLabel.setText("Order deleted.");
                loadOrders();
            } else {
                statusLabel.setText("Failed to delete order.");
            }
        } else {
            statusLabel.setText("Select an order to delete.");
        }
    }


    public static class OrderView {
        private final int orderId;
        private final int clientId;
        private final String clientName;
        private final int productId;
        private final String productName;
        private final int quantity;
        private final BigDecimal price;

        public OrderView(int orderId, int clientId, String clientName, int productId, String productName, int quantity, BigDecimal price) {
            this.orderId = orderId;
            this.clientId = clientId;
            this.clientName = clientName;
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        public int getOrderId() { return orderId; }
        public int getClientId() { return clientId; }
        public String getClientName() { return clientName; }
        public int getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public BigDecimal getPrice() { return price; }
    }

}
