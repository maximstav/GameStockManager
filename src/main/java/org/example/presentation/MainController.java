package org.example.presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;

public class MainController {

    @FXML private StackPane contentArea;
    @FXML private Button clientsButton;
    @FXML private Button productsButton;
    @FXML private Button ordersButton;

    @FXML
    public void initialize() {
        loadView("view/client_view.fxml");

        clientsButton.setOnAction(e -> loadView("view/client_view.fxml"));
        productsButton.setOnAction(e -> loadView("view/product_view.fxml"));
        ordersButton.setOnAction(e -> loadView("view/order_view.fxml"));
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlPath));
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
