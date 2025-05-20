package org.example.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.bussinessLayer.ClientService;
import org.example.model.Client;

public class ClientController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private Button addClientButton;

    @FXML
    private TableView<Client> clientTable;

    private final ClientService clientService = new ClientService();

    @FXML
    public void initialize() {
        setupTable();
        loadClients();

        addClientButton.setOnAction(event -> handleAddClient());
    }

    private void setupTable() {
        TableColumn<Client, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<Client, String> nameCol = new TableColumn<>("Username");
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));

        TableColumn<Client, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<Client, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole()));

        clientTable.getColumns().setAll(idCol, nameCol, emailCol, roleCol);
    }

    private void loadClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList(clientService.getAllClients());
        clientTable.setItems(clients);
    }

    private void handleAddClient() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username and Email cannot be empty.");
            return;
        }

        Client newClient = new Client(0, name, email, "CLIENT");

        clientService.registerClient(newClient);
        loadClients();
        nameField.clear();
        emailField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

