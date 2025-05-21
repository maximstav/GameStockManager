package org.example.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.bussinessLayer.ClientService;
import org.example.model.Client;

/**
 * JavaFX controller for managing clients.
 * Handles adding, updating, and deleting clients via the GUI.
 */
public class ClientController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private Button addClientButton;

    @FXML
    private Button updateClientButton;

    @FXML
    private Button deleteClientButton;


    @FXML
    private TableView<Client> clientTable;

    private final ClientService clientService = new ClientService();

    /**
     * Initializes the controller after the FXML elements are loaded.
     * Sets up table columns and loads all clients from the database.
     */
    @FXML
    public void initialize() {
        setupTable();
        loadClients();

        addClientButton.setOnAction(event -> handleAddClient());
        updateClientButton.setOnAction(event -> handleUpdateClient());
        deleteClientButton.setOnAction(event -> handleDeleteClient());

        clientTable.setOnMouseClicked(event -> {
            Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                nameField.setText(selectedClient.getUsername());
                emailField.setText(selectedClient.getEmail());
            }
        });
    }

    /**
     * Configures the columns of the client table view.
     */
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

    /**
     * Loads all clients from the database and displays them in the table.
     */
    private void loadClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList(clientService.getAllClients());
        clientTable.setItems(clients);
    }

    /**
     * Handles adding a new client using input from the text fields.
     */
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

    private void handleUpdateClient() {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a client to update.");
            return;
        }

        String updatedName = nameField.getText().trim();
        String updatedEmail = emailField.getText().trim();

        if (updatedName.isEmpty() || updatedEmail.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username and Email cannot be empty.");
            return;
        }

        selectedClient.setUsername(updatedName);
        selectedClient.setEmail(updatedEmail);
        clientService.updateClient(selectedClient);
        loadClients();
        nameField.clear();
        emailField.clear();
    }

    private void handleDeleteClient() {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a client to delete.");
            return;
        }

        clientService.deleteClient(selectedClient.getId());
        loadClients();
        nameField.clear();
        emailField.clear();
    }


    /**
     * Utility method to display alerts.
     *
     * @param type    the type of alert
     * @param title   the alert title
     * @param message the alert message
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

